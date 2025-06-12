package dev.luiiscarlos.academ_iq_api.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

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

	@Nullable
	@Builder.Default
	private Integer statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

	@Nullable
	@Builder.Default
	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

	@Nullable
	@Builder.Default
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime timestamp = LocalDateTime.now();

	@Nullable
	@Builder.Default
	private String message = "An error has ocurred";

}
