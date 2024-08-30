package com.banca.fibonacci.controller.advice;

import com.banca.fibonacci.models.dtos.ErrorResponseDTO;
import com.banca.fibonacci.utils.DateUtils;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GenericControllerAdvice  extends ResponseEntityExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(GenericControllerAdvice.class);

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(DateUtils.formatCurrentDateTime())
                .message("El parámetro debe ser un número entero válido.")
                .description(ex.getMessage())
                .build();
        LOGGER.error("error trying to convert parameter: ", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(DateUtils.formatCurrentDateTime())
                .message("El valor ingresado debe ser un número positivo.")
                .description(ex.getMessage())
                .build();
        LOGGER.error("error trying to validate request body: ", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(DateUtils.formatCurrentDateTime())
                .message("El recurso solicitado no se encontró.")
                .description("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL())
                .build();
        LOGGER.error("error trying to find handler: ", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ErrorResponseDTO> handleRequestNotPermitted(RequestNotPermitted ex) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .statusCode(HttpStatus.TOO_MANY_REQUESTS.value())
                .timestamp(DateUtils.formatCurrentDateTime())
                .message("Rate limit exceeded")
                .description("You have exceeded the maximum number of allowed requests. Please try again later.")
                .build();
        LOGGER.error("error trying to handle request: ", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }
}
