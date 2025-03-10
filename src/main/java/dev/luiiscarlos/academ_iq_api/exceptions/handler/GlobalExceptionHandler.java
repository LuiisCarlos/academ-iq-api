package dev.luiiscarlos.academ_iq_api.exceptions.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.luiiscarlos.academ_iq_api.exceptions.CourseNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidPasswordException;
import dev.luiiscarlos.academ_iq_api.exceptions.RefreshTokenExpiredException;
import dev.luiiscarlos.academ_iq_api.exceptions.RefreshTokenNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.RoleNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.StorageException;
import dev.luiiscarlos.academ_iq_api.exceptions.StorageFileNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.StorageNoFileTypeFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserAlreadyRegisteredException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserRegistrationWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.exceptions.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({UserNotFoundException.class,
		RoleNotFoundException.class,
		AuthenticationCredentialsNotFoundException.class,
		StorageFileNotFoundException.class,
		RefreshTokenNotFoundException.class,
		StorageNoFileTypeFoundException.class,
		CourseNotFoundException.class})
	public ResponseEntity<ErrorResponse> handleNotFound(Exception ex) {
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(ErrorResponse.builder()
				.status(HttpStatus.NOT_FOUND)
				.message(ex.getMessage()).build());
	}


	@ExceptionHandler({UserAlreadyRegisteredException.class,
		UserRegistrationWithDifferentPasswordsException.class,
		InvalidPasswordException.class,
		StorageException.class})
	public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.message(ex.getMessage()).build());
	}

    @ExceptionHandler({InvalidCredentialsException.class, RefreshTokenExpiredException.class})
	public ResponseEntity<ErrorResponse> handleUnauthorized(Exception ex) {
		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.message(ex.getMessage()).build());
	}


	@ExceptionHandler({JwtValidationException.class, BadJwtException.class})
	public ResponseEntity<ErrorResponse> handleForbidden(Exception ex) {
		return ResponseEntity
			.status(HttpStatus.FORBIDDEN)
			.body(ErrorResponse.builder()
				.status(HttpStatus.FORBIDDEN)
				.message(ex.getMessage()).build());
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex,
            @Nullable Object body,
            @NonNull HttpHeaders headers,
			@NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request) {
		return ResponseEntity
			.status(statusCode)
			.headers(headers)
			.body(ErrorResponse.builder()
				.status(HttpStatus.valueOf(statusCode.value()))
				.message(ex.getCause().getMessage()).build());
	}

}
