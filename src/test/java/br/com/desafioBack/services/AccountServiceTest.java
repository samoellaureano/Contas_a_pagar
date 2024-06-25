package br.com.desafioBack.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.desafioBack._shared.AccountTestUtil;
import br.com.desafioBack.entity.Account;
import br.com.desafioBack.repository.AccountRepository;
import br.com.desafioBack.repository.Situation;
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
class AccountServiceTest
{

	@InjectMocks
	private AccountService service;

	@Mock
	private AccountRepository repository;

	private Account entity;

	@BeforeEach
	public void setUp()
	{
		entity = new AccountTestUtil().createAccount();
	}

	@Test
	void findByIdTest()
	{
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		Optional<Account> account = service.findById(1L);
		verify(repository).findById(anyLong());
		assertEquals(account.get(), entity);
	}

	@Test
	void changeStatusTest()
	{
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(entity);
		Account account = service.changeStatus(1L, Situation.PAID);
		verify(repository).findById(anyLong());
		verify(repository).save(entity);
		assertEquals(Situation.PAID, account.getSituation());
	}

	@Test
	void saveTest()
	{
		when(repository.save(entity)).thenReturn(entity);
		Account account = service.save(entity);
		verify(repository).save(entity);
		assertEquals(account, entity);
	}

	@Test
	void updateTest()
	{
		when(repository.save(entity)).thenReturn(entity);
		Account account = service.update(entity);
		verify(repository).save(entity);
		assertEquals(account, entity);
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
		when(repository.findAllByDueDateBeforeAndDescriptionContainingIgnoreCaseAndSituationEquals(any(),
			any(), any(), any(Pageable.class))).thenReturn(Page.empty());
		service.findAllContasAPagar(entity.getDueDate(), entity.getDescription(), 0, 10);
		verify(repository).findAllByDueDateBeforeAndDescriptionContainingIgnoreCaseAndSituationEquals(
			any(), any(), any(), any(Pageable.class));
	}

	@Test
	void findValueAmountPaidTest()
	{
		when(repository.findAllBySituationEqualsAndPaymentDateIsAfterAndPaymentDateBefore(any(), any(),
			any())).thenReturn(List.of(entity));
		service.findValueAmountPaid(entity.getPaymentDate().toString(),
			entity.getPaymentDate().toString());
		verify(repository).findAllBySituationEqualsAndPaymentDateIsAfterAndPaymentDateBefore(any(),
			any(), any());
	}
}