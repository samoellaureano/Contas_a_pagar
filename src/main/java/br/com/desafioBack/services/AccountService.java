package br.com.desafioBack.services;

import br.com.desafioBack.entity.Account;
import br.com.desafioBack.exception.ConverterException;
import br.com.desafioBack.exception.ImportingException;
import br.com.desafioBack.repository.AccountRepository;
import br.com.desafioBack.repository.Situation;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AccountService
{
	private final AccountRepository accountRepository;

	public AccountService(AccountRepository accountRepository)
	{
		this.accountRepository = accountRepository;
	}

	public Account save(Account account)
	{
		return accountRepository.save(account);
	}

	public Account update(Account account)
	{
		return accountRepository.save(account);
	}

	public Account changeStatus(Long id, Situation situation)
	{
		Account account = accountRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Conta não encontrada"));
		account.setSituation(situation);
		return accountRepository.save(account);
	}

	public Page<Account> findAll(int page, int size)
	{
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		return accountRepository.findAll(pageable);
	}

	public Page<Account> findAllContasAPagar(LocalDate dataVencimento, String descricao, int page,
		int size)
	{
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		Situation situation = Situation.PENDING;
		return accountRepository.findAllByDueDateBeforeAndDescriptionContainingIgnoreCaseAndSituationEquals(
			dataVencimento, descricao, situation, pageable);
	}

	public Optional<Account> findById(long id)
	{
		return accountRepository.findById(id);
	}

	public List<Account> importCSV(MultipartFile file)
	{
		try (BufferedReader reader = new BufferedReader(
			new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)))
		{
			List<Account> createdAccounts = new ArrayList<>();
			String line;
			reader.readLine();
			while ((line = reader.readLine()) != null)
			{
				String[] data = line.split(";");
				Account account = new Account();
				account.setDescription(data[0]);
				account.setDueDate(parseData(data[1], "dd/MM/yyyy"));
				account.setValue(new BigDecimal(data[2]));
				if (data[3].matches("\\d+"))
				{
					account.setSituation(Situation.values()[Integer.parseInt(data[3])]);
				}
				else
				{
					account.setSituation(Situation.valueOf(data[3].toUpperCase()));
				}
				accountRepository.save(account);
				createdAccounts.add(account);
			}
			return createdAccounts;
		}
		catch (Exception e)
		{
			throw new ImportingException("Error importing file");
		}
	}

	public static LocalDate parseData(String data, String formato)
	{
		try
		{
			String[] dataSplit = data.split("[/-]");

			if (formato.equals("dd/MM/yyyy"))
			{
				return LocalDate.of(Integer.parseInt(dataSplit[2]), Integer.parseInt(dataSplit[1]),
					Integer.parseInt(dataSplit[0]));
			}
			else
			{
				return LocalDate.of(Integer.parseInt(dataSplit[0]), Integer.parseInt(dataSplit[1]),
					Integer.parseInt(dataSplit[2]));
			}
		}
		catch (Exception e)
		{
			throw new ConverterException("Erro ao converter data");
		}
	}

	public BigDecimal findValueAmountPaid(String dataPagamentoInicial, String dataPagamentoFinal)
	{
		return accountRepository.findAllBySituationEqualsAndPaymentDateIsAfterAndPaymentDateBefore(
				Situation.PAID, parseData(dataPagamentoInicial, "yyyy/MM/dd"),
				parseData(dataPagamentoFinal, "yyyy/MM/dd")).stream().map(Account::getValue)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}