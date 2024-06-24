package br.com.desafioBack;

import br.com.desafioBack.entity.Conta;
import br.com.desafioBack.repository.Situacao;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaTesteUtil
{
	protected Conta createConta()
	{
		Conta conta = new Conta();
		conta.setDataVencimento(LocalDate.parse("2021-01-01"));
		conta.setDataPagamento(LocalDate.parse("2021-01-01"));
		conta.setValor(new BigDecimal("100.00"));
		conta.setDescricao("Conta de luz");
		conta.setSituacao(Situacao.PENDENTE);
		return conta;
	}
}
