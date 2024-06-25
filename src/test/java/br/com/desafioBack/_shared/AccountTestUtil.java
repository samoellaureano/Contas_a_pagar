package br.com.desafioBack._shared;

import br.com.desafioBack.entity.Account;
import br.com.desafioBack.repository.Situation;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountTestUtil
{
	public Account createAccount()
	{
		Account account = new Account();
		account.setDueDate(LocalDate.parse("2021-01-01"));
		account.setPaymentDate(LocalDate.parse("2021-01-01"));
		account.setValue(new BigDecimal("100.00"));
		account.setDescription("Test");
		account.setSituation(Situation.PENDING);
		return account;
	}
}
