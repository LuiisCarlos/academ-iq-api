package dev.luiiscarlos.academ_iq_api.exceptions.handlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.luiiscarlos.academ_iq_api.exceptions.AuthCredentialsNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.CourseNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidPasswordException;
import dev.luiiscarlos.academ_iq_api.exceptions.RefreshTokenExpiredException;
import dev.luiiscarlos.academ_iq_api.exceptions.RefreshTokenNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.RoleNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.FileStorageException;
import dev.luiiscarlos.academ_iq_api.exceptions.FileNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserAlreadyRegisteredException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserUnderageException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({UserNotFoundException.class,
		RoleNotFoundException.class,
		AuthCredentialsNotFoundException.class,
		FileNotFoundException.class,
		RefreshTokenNotFoundException.class,
		CourseNotFoundException.class})
	public ResponseEntity<ErrorResponse> handleNotFound(Exception ex) {
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(ErrorResponse.builder()
				.status(HttpStatus.NOT_FOUND)
				.statusCode(HttpStatus.NOT_FOUND.value())
				.message(ex.getMessage()).build());
	}

	@ExceptionHandler({UserAlreadyRegisteredException.class,
		UserWithDifferentPasswordsException.class,
		InvalidPasswordException.class,
		FileStorageException.class})
	public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.message(ex.getMessage()).build());
	}

    @ExceptionHandler({AuthenticationException.class, InvalidCredentialsException.class})
	public ResponseEntity<ErrorResponse> handleUnauthorized(Exception ex) {
		return ResponseEntity
			.status(HttpStatus.UNAUTHORIZED)
			.body(ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.statusCode(HttpStatus.UNAUTHORIZED.value())
				.message(ex.getMessage()).build());
	}

	@ExceptionHandler({JwtValidationException.class,
		BadJwtException.class,
		UserUnderageException.class,
		RefreshTokenExpiredException.class,
		AccessDeniedException.class})
	public ResponseEntity<ErrorResponse> handleForbidden(Exception ex) {
		return ResponseEntity
			.status(HttpStatus.FORBIDDEN)
			.body(ErrorResponse.builder()
				.status(HttpStatus.FORBIDDEN)
				.statusCode(HttpStatus.FORBIDDEN.value())
				.message(ex.getMessage()).build());
	}

	@Override
	@ExceptionHandler({Exception.class})
	protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex,
            @Nullable Object body,
            @NonNull HttpHeaders headers,
			@NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request) {
		String message = ex.getCause() != null ? ex.getCause().getMessage() : null;
		return ResponseEntity
			.status(statusCode)
			.headers(headers)
			.body(ErrorResponse.builder()
				.status(HttpStatus.valueOf(statusCode.value()))
				.statusCode(statusCode.value())
				.message(message));
	}

}
