package dev.luiiscarlos.academ_iq_api.shared.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

	@Builder.Default
	private Integer statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

	@Builder.Default
	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

	@Builder.Default
	@JsonFormat(shape = Shape.STRING)
	private LocalDateTime timestamp = LocalDateTime.now();

	@Builder.Default
	private String message = ErrorMessages.UNEXPECTED_ERROR;

}
