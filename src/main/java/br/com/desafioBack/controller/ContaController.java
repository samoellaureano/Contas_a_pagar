package br.com.desafioBack.controller;

import br.com.desafioBack.entity.Conta;
import br.com.desafioBack.exception.SituacaoConversionException;
import br.com.desafioBack.repository.Situacao;
import br.com.desafioBack.services.ContaService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/contas")
public class ContaController
{
	private static final Logger logger = LoggerFactory.getLogger(ContaController.class);

	@Autowired
	private ContaService contaService;

	@PostMapping
	public ResponseEntity<Conta> create(@RequestBody Conta conta)
	{
		logger.info("Criando uma nova conta");
		return ResponseEntity.ok(contaService.save(conta));
	}

	@GetMapping
	public ResponseEntity<Page<Conta>> findAll(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size)
	{
		logger.info("Listando todas as contas");
		return ResponseEntity.ok(contaService.findAll(page, size));
	}

	@GetMapping("/contasAPagar")
	public ResponseEntity<Page<Conta>> findAllContasAPagar(
		@RequestParam(required = false) String descricao,
		@RequestParam(required = false) String dataVencimento,
		@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
	{
		logger.info("Buscando contas com descrição {} e data de vencimento {}", descricao,
			dataVencimento);
		return ResponseEntity.ok(
			contaService.findAllContasAPagar(LocalDate.parse(dataVencimento), descricao, page, size));
	}

	@PutMapping
	public ResponseEntity<Conta> update(@RequestParam() Long id, @RequestBody Conta conta)
	{
		logger.info("Atualizando a conta com id {}", id);
		return ResponseEntity.ok(contaService.update(conta));
	}

	@PatchMapping
	public ResponseEntity<Conta> changeStatus(@RequestParam Long id, @RequestParam String situacao)
	{
		logger.info("Alterando a situação da conta com id {}", id);
		if (situacao == null)
		{
			throw new SituacaoConversionException("null");
		}
		if (situacao.matches("[0-9]+"))
		{
			return ResponseEntity.ok(
				contaService.changeStatus(id, Situacao.values()[Integer.parseInt(situacao)]));
		}
		try
		{
			Situacao situacaoEnum = Situacao.valueOf(situacao.toUpperCase());
			return ResponseEntity.ok(contaService.changeStatus(id, situacaoEnum));
		}
		catch (IllegalArgumentException ex)
		{
			throw new SituacaoConversionException(situacao);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Conta>> findContaById(@PathVariable Long id)
	{
		logger.info("Buscando a conta com id {}", id);
		return ResponseEntity.ok(contaService.findById(id));
	}

	@GetMapping("/valorTotalPago")
	public ResponseEntity<BigDecimal> findValorTotalPago(@RequestParam() String dataPagamentoInicial,
		@RequestParam() String dataPagamentoFinal)
	{
		logger.info("Buscando o valor total das contas pagas");
		return ResponseEntity.ok(
			contaService.findValorTotalPago(dataPagamentoInicial, dataPagamentoFinal));
	}

	@PostMapping("/import")
	public ResponseEntity<List<Conta>> importCSV(@RequestParam("file") MultipartFile file)
	{
		return ResponseEntity.ok(contaService.importCSV(file));
	}
}