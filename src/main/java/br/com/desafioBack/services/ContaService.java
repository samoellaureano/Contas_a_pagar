package br.com.desafioBack.services;

import br.com.desafioBack.entity.Conta;
import br.com.desafioBack.exception.ConverterException;
import br.com.desafioBack.exception.ImportacaoException;
import br.com.desafioBack.repository.ContaRepository;
import br.com.desafioBack.repository.Situacao;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ContaService
{
	@Autowired
	private ContaRepository contaRepository;

	public Conta save(Conta conta)
	{
		return contaRepository.save(conta);
	}

	public Conta update(Conta conta)
	{
		return contaRepository.save(conta);
	}

	public Conta changeStatus(Long id, Situacao situacao)
	{
		Conta conta = contaRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Conta não encontrada"));
		conta.setSituacao(situacao);
		return contaRepository.save(conta);
	}

	public PageImpl<Conta> findAll(int page, int size)
	{
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		PageImpl<Conta> contas = contaRepository.findAll(pageable);
		return contas;
	}

	public PageImpl<Conta> findAllContasAPagar(LocalDate dataVencimento, String descricao, int page,
		int size)
	{
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		Situacao situacao = Situacao.PENDENTE;
		return contaRepository.findAllByDataVencimentoBeforeAndDescricaoContainingIgnoreCaseAndSituacaoEquals(
			dataVencimento, descricao, situacao, pageable);
	}

	public Conta findContaById(long id)
	{
		return contaRepository.findContaById(id);
	}

	public List<Conta> importCSV(MultipartFile file)
	{
		try (BufferedReader reader = new BufferedReader(
			new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)))
		{
			List<Conta> contasCriadas = new ArrayList<>();
			String line;
			reader.readLine();
			while ((line = reader.readLine()) != null)
			{
				String[] data = line.split(";");
				Conta conta = new Conta();
				conta.setDescricao(data[0]);
				conta.setDataVencimento(parseData(data[1], "dd/MM/yyyy"));
				conta.setValor(new BigDecimal(data[2]));
				if (data[3].matches("[0-9]+"))
				{
					conta.setSituacao(Situacao.values()[Integer.parseInt(data[3])]);
				}
				else
				{
					conta.setSituacao(Situacao.valueOf(data[3].toUpperCase()));
				}
				contaRepository.save(conta);
				contasCriadas.add(conta);
			}
			return contasCriadas;
		}
		catch (Exception e)
		{
			throw new ImportacaoException("Erro ao importar arquivo CSV");
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

	public BigDecimal findValorTotalPago(String dataPagamentoInicial, String dataPagamentoFinal)
	{
		return contaRepository.findAllBySituacaoEqualsAndDataPagamentoIsAfterAndDataPagamentoBefore(
				Situacao.PAGO, parseData(dataPagamentoInicial, "yyyy/MM/dd"),
				parseData(dataPagamentoFinal, "yyyy/MM/dd")).stream().map(Conta::getValor)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}