package br.com.desafioBack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SituacaoConversionException extends RuntimeException
{

	public SituacaoConversionException(String situacao)
	{
		super("Valor inválido para a situação: " + situacao);
	}
}