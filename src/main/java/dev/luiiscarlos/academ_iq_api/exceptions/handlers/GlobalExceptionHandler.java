package dev.luiiscarlos.academ_iq_api.exceptions.handlers;

import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.luiiscarlos.academ_iq_api.exceptions.*;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({
			UserNotFoundException.class,
			RoleNotFoundException.class,
			AuthCredentialsNotFoundException.class,
			FileNotFoundException.class,
			RefreshTokenNotFoundException.class,
			CourseNotFoundException.class,
			EnrollmentNotFoundException.class,
			SectionNotFoundException.class })
	public ResponseEntity<ErrorResponse> handleNotFoundExceptions(Exception ex) {
		String message = ex.getCause() != null
				? ex.getCause().getLocalizedMessage()
				: ex.getMessage();

		ErrorResponse error = ErrorResponse.builder()
				.status(HttpStatus.NOT_FOUND)
				.statusCode(HttpStatus.NOT_FOUND.value())
				.message(message)
				.build();

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({
			UserAlreadyExistsException.class,
			UserWithDifferentPasswordsException.class,
			InvalidPasswordException.class,
			FileStorageException.class,
			CourseAlreadyExistsEception.class,
			InvalidRatingException.class,
			RatingNotFoundException.class, })
	public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception ex) {
		String message = ex.getCause() != null
				? ex.getCause().getLocalizedMessage()
				: ex.getMessage();

		ErrorResponse error = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.message(message)
				.build();

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({
			AuthenticationException.class,
			InvalidTokenException.class,
			InvalidCredentialsException.class })
	public ResponseEntity<ErrorResponse> handleUnauthorizedExceptions(Exception ex) {
		String message = ex.getCause() != null
				? ex.getCause().getLocalizedMessage()
				: ex.getMessage();

		ErrorResponse error = ErrorResponse.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.statusCode(HttpStatus.UNAUTHORIZED.value())
				.message(message)
				.build();

		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({
			JwtValidationException.class,
			BadJwtException.class,
			UserUnderageException.class,
			RefreshTokenExpiredException.class,
			AccessDeniedException.class,
			UserAccountNotVerifiedException.class,
			InvalidFileTypeException.class })
	public ResponseEntity<ErrorResponse> handleForbiddenExceptions(Exception ex) {
		String message = ex.getCause() != null
				? ex.getCause().getLocalizedMessage()
				: ex.getMessage();

		ErrorResponse error = ErrorResponse.builder()
				.status(HttpStatus.FORBIDDEN)
				.statusCode(HttpStatus.FORBIDDEN.value())
				.message(message)
				.build();

		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler({
			RatingAlreadyExistsException.class,
			DataIntegrityViolationException.class })
	public ResponseEntity<ErrorResponse> handleConflictExceptions(Exception ex) {
		String message = ex.getCause() != null
				? ex.getCause().getLocalizedMessage()
				: ex.getMessage();

		ErrorResponse error = ErrorResponse.builder()
				.status(HttpStatus.CONFLICT)
				.statusCode(HttpStatus.CONFLICT.value())
				.message(message)
				.build();

		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(TransactionSystemException.class)
	public ResponseEntity<ErrorResponse> handleTransactionExceptions(Exception ex) {
		String message = "Failed to process database transaction: " +
				"Operation could not be completed, please try again later";

		ErrorResponse error = ErrorResponse.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(message)
				.build();

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
		String message = ex.getCause() != null
				? ex.getCause().getLocalizedMessage()
				: ex.getMessage();

		ErrorResponse error = ErrorResponse.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(message)
				.build();

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			@NonNull Exception ex,
			@Nullable Object body,
			@NonNull HttpHeaders headers,
			@NonNull HttpStatusCode statusCode,
			@NonNull WebRequest request) {
		String message = ex.getCause() != null
				? ex.getCause().getLocalizedMessage()
				: ex.getMessage();

		ErrorResponse error = ErrorResponse.builder()
				.status(HttpStatus.valueOf(statusCode.value()))
				.statusCode(statusCode.value())
				.message(message)
				.build();

		return new ResponseEntity<>(error, headers, statusCode);
	}

}
