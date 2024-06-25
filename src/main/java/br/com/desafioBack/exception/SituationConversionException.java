package br.com.desafioBack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SituationConversionException extends RuntimeException
{

	public SituationConversionException(String situation)
	{
		super("Value not found for situation: " + situation);
	}
}