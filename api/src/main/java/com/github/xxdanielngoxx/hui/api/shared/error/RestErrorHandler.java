package com.github.xxdanielngoxx.hui.api.shared.error;

import io.jsonwebtoken.JwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {

  @Nonnull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @Nonnull HttpHeaders headers,
      @Nonnull HttpStatusCode status,
      @Nonnull WebRequest request) {

    final Stream<String> fieldErrors =
        ex.getFieldErrors().stream()
            .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()));

    final Stream<String> globalErrors =
        ex.getGlobalErrors().stream()
            .map(
                error -> String.format("%s: %s", error.getObjectName(), error.getDefaultMessage()));

    final List<String> errors = Stream.concat(fieldErrors, globalErrors).toList();

    final ApiError apiError =
        ApiError.builder()
            .status(HttpStatus.BAD_REQUEST)
            .errors(errors)
            .path(extractRequestURI(request))
            .build();

    return new ResponseEntity<>(apiError, headers, apiError.getStatus());
  }

  @Nonnull
  private String extractRequestURI(@Nonnull final WebRequest request) {
    final ServletWebRequest servletWebRequest = (ServletWebRequest) request;
    return servletWebRequest.getRequest().getRequestURI();
  }

  @ExceptionHandler(value = {IllegalArgumentException.class})
  public ResponseEntity<ApiError> handleIllegalArgumentException(
      @Nonnull IllegalArgumentException ex, @Nonnull HttpServletRequest request) {

    final ApiError apiError =
        ApiError.builder()
            .status(HttpStatus.BAD_REQUEST)
            .errors(List.of(ex.getMessage()))
            .path(request.getRequestURI())
            .build();

    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler(value = {BadCredentialsException.class})
  public ResponseEntity<ApiError> handleBadCredentialsException(
      @Nonnull BadCredentialsException ex, @Nonnull HttpServletRequest request) {

    final ApiError apiError =
        ApiError.builder()
            .status(HttpStatus.UNAUTHORIZED)
            .errors(List.of(ex.getMessage()))
            .path(request.getRequestURI())
            .build();

    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler(value = {JwtException.class})
  public ResponseEntity<ApiError> handleJwtException(
      @Nonnull JwtException ex, @Nonnull HttpServletRequest request) {

    final ApiError apiError =
        ApiError.builder()
            .status(HttpStatus.UNAUTHORIZED)
            .errors(List.of(ex.getMessage()))
            .path(request.getRequestURI())
            .build();

    log.error("[{}]: {}", apiError.getErrorId(), ex.getMessage(), ex);

    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      @Nonnull HttpMessageNotReadableException ex,
      @Nonnull HttpHeaders headers,
      @Nonnull HttpStatusCode status,
      @Nonnull WebRequest request) {
    log.error("{}", ex.getMessage(), ex);
    return super.handleHttpMessageNotReadable(ex, headers, status, request);
  }

  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<Object> fallback(@Nonnull Exception ex, @Nonnull WebRequest request) {

    final ApiError apiError =
        ApiError.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .errors(List.of(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()))
            .path(extractRequestURI(request))
            .build();

    log.error("[{}]: {}", apiError.getErrorId(), ex.getMessage(), ex);

    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
