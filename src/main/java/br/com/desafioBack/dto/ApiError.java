package br.com.desafioBack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError
{
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private LocalDateTime timestamp;
	private int status;
	private String error;
	private String message;

	public ApiError(HttpStatus status, String message)
	{
		this.timestamp = LocalDateTime.now();
		this.status = status.value();
		this.error = status.getReasonPhrase();
		this.message = message;
	}
}