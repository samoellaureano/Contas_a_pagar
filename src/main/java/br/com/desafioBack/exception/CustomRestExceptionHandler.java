package br.com.desafioBack.exception;

import br.com.desafioBack.dto.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler
{
	private static final Logger logger = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

	@ExceptionHandler(SituacaoConversionException.class)
	protected ResponseEntity<Object> handleSituacaoConversionException(SituacaoConversionException ex,
		WebRequest request)
	{
		logger.error("Erro durante o processamento da requisição", ex);
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
		return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request)
	{
		logger.error("Erro durante o processamento da requisição", ex);
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
			"Ocorreu um erro durante o processamento da requisição.");
		return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
			request);
	}
}