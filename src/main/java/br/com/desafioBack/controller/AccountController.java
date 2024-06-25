package br.com.desafioBack.controller;

import br.com.desafioBack.entity.Account;
import br.com.desafioBack.exception.SituationConversionException;
import br.com.desafioBack.repository.Situation;
import br.com.desafioBack.services.AccountService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/accounts")
public class AccountController
{
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private final AccountService accountService;

	public AccountController(AccountService accountService)
	{
		this.accountService = accountService;
	}

	@PostMapping
	public ResponseEntity<Account> create(@RequestBody Account account)
	{
		logger.info("Creating account");
		return ResponseEntity.ok(accountService.save(account));
	}

	@GetMapping
	public ResponseEntity<Page<Account>> findAll(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size)
	{
		logger.info("Listing all accounts");
		return ResponseEntity.ok(accountService.findAll(page, size));
	}

	@GetMapping("/accountsToPay")
	public ResponseEntity<Page<Account>> findAllAccountsToPay(
		@RequestParam(required = false) String description,
		@RequestParam(required = false) String dueDate, @RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size)
	{
		logger.info("Getting accounts with description {} and due date {}", description, dueDate);
		return ResponseEntity.ok(
			accountService.findAllContasAPagar(LocalDate.parse(dueDate), description, page, size));
	}

	@PutMapping
	public ResponseEntity<Account> update(@RequestParam() Long id, @RequestBody Account account)
	{
		logger.info("Updating account with id {}", id);
		return ResponseEntity.ok(accountService.update(account));
	}

	@PatchMapping
	public ResponseEntity<Account> changeStatus(@RequestParam Long id, @RequestParam String situation)
	{
		logger.info("Changing account status with id {}", id);
		if (situation == null)
		{
			throw new SituationConversionException("null");
		}
		if (situation.matches("\\d+"))
		{
			return ResponseEntity.ok(
				accountService.changeStatus(id, Situation.values()[Integer.parseInt(situation)]));
		}
		try
		{
			Situation situationEnum = Situation.valueOf(situation.toUpperCase());
			return ResponseEntity.ok(accountService.changeStatus(id, situationEnum));
		}
		catch (IllegalArgumentException ex)
		{
			throw new SituationConversionException(situation);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Account>> findAccountById(@PathVariable Long id)
	{
		logger.info("Getting account with id {}", id);
		return ResponseEntity.ok(accountService.findById(id));
	}

	@GetMapping("/valueAmountPaid")
	public ResponseEntity<BigDecimal> findValueAmountPaid(@RequestParam() String paymentStartDate,
		@RequestParam() String paymentEndDate)
	{
		logger.info("Getting total value paid between {} and {}", paymentStartDate, paymentEndDate);
		return ResponseEntity.ok(accountService.findValueAmountPaid(paymentStartDate, paymentEndDate));
	}

	@PostMapping("/import")
	public ResponseEntity<List<Account>> importCSV(@RequestParam("file") MultipartFile file)
	{
		return ResponseEntity.ok(accountService.importCSV(file));
	}
}