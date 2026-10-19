
package com.becoder.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(),message, request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}

	// Category not found
	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCategoryNotFound(CategoryNotFoundException ex,HttpServletRequest request) {
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
	}

	// Duplicate category name
	@ExceptionHandler(DuplicateCategoryException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateCategory(DuplicateCategoryException ex,HttpServletRequest request) {
		return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
	}

	// Invalid @Valid request fields
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,HttpServletRequest request) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).distinct()
				.collect(Collectors.joining("; "));

		if (message.isBlank()) {
			message = "Request validation failed";
		}

		return buildResponse(HttpStatus.BAD_REQUEST, message, request);
	}

	// Other binding errors
	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleBinding(BindException ex, HttpServletRequest request) {

		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).distinct()
				.collect(Collectors.joining("; "));

		if (message.isBlank()) {
			message = "Invalid request parameters";
		}

		return buildResponse(HttpStatus.BAD_REQUEST, message, request);
	}

	// Constraint validation failures
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex,HttpServletRequest request) {
		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	}

	// Malformed JSON or an invalid JSON value
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex,HttpServletRequest request) {
		return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request body. Please check your JSON and field values.",request);
	}

	// Database constraint violations
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex,HttpServletRequest request) {
		return buildResponse(HttpStatus.CONFLICT, "The request conflicts with existing database data.", request);
	}

	// Other invalid arguments
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,HttpServletRequest request) {
		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	}

	// Unexpected errors
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {

		// Log the full exception on the server for debugging.
		ex.printStackTrace();

		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.",
				request);
	}
	
	@ExceptionHandler(SupplierNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleSupplierNotFound(SupplierNotFoundException ex,HttpServletRequest request) {
		return buildResponse(HttpStatus.NOT_FOUND,ex.getMessage(),request);
	}

}
