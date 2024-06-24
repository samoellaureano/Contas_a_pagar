package br.com.desafioBack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.desafioBack.entity.Conta;
import br.com.desafioBack.repository.ContaRepository;
import br.com.desafioBack.repository.Situacao;
import br.com.desafioBack.services.ContaService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ContaServiceTest
{

	@InjectMocks
	private ContaService service;

	@Mock
	private ContaRepository repository;

	private Conta entity;

	@BeforeEach
	public void setUp()
	{
		entity = new ContaTesteUtil().createConta();
	}

	@Test
	void findByIdTest()
	{
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		Optional<Conta> conta = service.findById(1L);
		verify(repository).findById(anyLong());
		assertEquals(conta.get(), entity);
	}

	@Test
	void changeStatusTest()
	{
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(entity);
		Conta conta = service.changeStatus(1L, Situacao.PAGO);
		verify(repository).findById(anyLong());
		verify(repository).save(entity);
		assertEquals(conta.getSituacao(), Situacao.PAGO);
	}

	@Test
	void saveTest()
	{
		when(repository.save(entity)).thenReturn(entity);
		Conta conta = service.save(entity);
		verify(repository).save(entity);
		assertEquals(conta, entity);
	}

	@Test
	void updateTest()
	{
		when(repository.save(entity)).thenReturn(entity);
		Conta conta = service.update(entity);
		verify(repository).save(entity);
		assertEquals(conta, entity);
	}

	@Test
	void findAllTest()
	{
		when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());
		service.findAll(0, 10);
		verify(repository).findAll(any(Pageable.class));
	}

	@Test
	void findAllContasAPagarTest()
	{
		when(repository.findAllByDataVencimentoBeforeAndDescricaoContainingIgnoreCaseAndSituacaoEquals(
			any(), any(), any(), any(Pageable.class))).thenReturn(Page.empty());
		service.findAllContasAPagar(entity.getDataVencimento(), entity.getDescricao(), 0, 10);
		verify(
			repository).findAllByDataVencimentoBeforeAndDescricaoContainingIgnoreCaseAndSituacaoEquals(
			any(), any(), any(), any(Pageable.class));
	}

	@Test
	void findValorTotalPagoTest()
	{
		when(
			repository.findAllBySituacaoEqualsAndDataPagamentoIsAfterAndDataPagamentoBefore(any(), any(),
				any())).thenReturn(List.of(entity));
		service.findValorTotalPago(entity.getDataPagamento().toString(),
			entity.getDataPagamento().toString());
		verify(repository).findAllBySituacaoEqualsAndDataPagamentoIsAfterAndDataPagamentoBefore(any(),
			any(), any());
	}
}
