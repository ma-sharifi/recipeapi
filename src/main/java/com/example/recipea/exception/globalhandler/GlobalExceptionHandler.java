package com.example.recipea.exception.globalhandler;


import com.example.recipea.exception.AbstractThrowable;
import com.example.recipea.exception.BadRequestException;
import com.example.recipea.exception.IngredientNotFoundException;
import com.example.recipea.exception.RecipeNotFoundException;
import com.example.recipea.service.dto.ResponseDto;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mahdi Sharifi
 */

@Order(Ordered.HIGHEST_PRECEDENCE) //https://www.toptal.com/java/spring-boot-rest-api-error-handling
@ControllerAdvice
@NoArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

//    @Profile("prod") // Because we need to know more abour out unhandled exception in developement
//    @ExceptionHandler(value = {Exception.class})
//    public ResponseEntity<ResponseDto<Void>> handleUnhandledException(Exception ex) {
//        log.error("#Unhandled exception occurred: " + ex.getMessage());
//        ResponseDto<Void> responseDto = new ResponseDto<>();
//        responseDto.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//        responseDto.setErrorCode(500);
//        responseDto.setMessage("Internal server occurred. please refer to log files.");
//        return ResponseEntity.status(responseDto.getHttpStatus()).body(responseDto);
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ResponseDto responseDto = ResponseDto.builder().httpStatus(HttpStatus.BAD_REQUEST).errorCode(4006)
                .message("#Validation method argument type mismatch error!").details(ex.getMessage() + " #parameter: " + ex.getParameter()).build();
        return new ResponseEntity<>(responseDto, responseDto.getHttpStatus());
    }

    /**
     * Constraint violation exception handler
     *
     * @param ex ConstraintViolationException
     * @return List<ValidationError> - list of ValidationError built
     * from set of ConstraintViolation
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        log.debug("Constraint violation exception encountered: {}", ex.getConstraintViolations(), ex);
        ResponseDto responseDto = ResponseDto.builder().httpStatus(HttpStatus.BAD_REQUEST).errorCode(4007)
                .message("#Constraint violation exception encountered!").details(ex.getMessage()).build();
        return new ResponseEntity<>(responseDto, responseDto.getHttpStatus());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseDto<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ResponseDto responseDto = ResponseDto.builder().httpStatus(HttpStatus.BAD_REQUEST)
                .message("#Validation method argument error!").errors(errors).errorCode(4009).build();
        if (log.isDebugEnabled())
            responseDto.setDetails(ex.getBindingResult().toString());
        return new ResponseEntity<>(responseDto, responseDto.getHttpStatus());
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ResponseDto<Void>> handleHttpRequestMethodNotSupportedException
            (HttpRequestMethodNotSupportedException ex, WebRequest request) {

        ResponseDto responseDto = ResponseDto.builder().httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .message("#Method Not Allowed!").errorCode(4050).build();
        if (log.isDebugEnabled())
            responseDto.setDetails(ex.getLocalizedMessage() + " ;" + request);
        return new ResponseEntity<>(responseDto, responseDto.getHttpStatus());
    }


    @ExceptionHandler(value = {EmptyResultDataAccessException.class})
    public ResponseEntity<ResponseDto<Void>> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {

        ResponseDto responseDto = ResponseDto.builder().httpStatus(HttpStatus.NOT_FOUND)
                .message("#This entity does not exists!!").errorCode(4043).build();
        if (log.isDebugEnabled())
            responseDto.setDetails(ex.getMessage());
        return new ResponseEntity<>(responseDto, responseDto.getHttpStatus());
    }

    @ExceptionHandler(value = {JdbcSQLIntegrityConstraintViolationException.class})
    public ResponseEntity<ResponseDto<Void>> handleJdbcSQLIntegrityConstraintViolationExceptionExceptions(JdbcSQLIntegrityConstraintViolationException ex) {

        ResponseDto responseDto = ResponseDto.builder().httpStatus(HttpStatus.BAD_REQUEST)
                .message("#Unique index or primary key violation!").errorCode(4005).build();
        if (log.isDebugEnabled())
            responseDto.setDetails(ex.getMessage() + " ;" + ex.getSQL());
        return new ResponseEntity<>(responseDto, responseDto.getHttpStatus());
    }

    @ExceptionHandler(value = {BadRequestException.class, RecipeNotFoundException.class, IngredientNotFoundException.class})
    public ResponseEntity<ResponseDto<Void>> handleException(AbstractThrowable ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(toDto(ex));
    }

    private ResponseDto<Void> toDto(AbstractThrowable exception) {
        ResponseDto<Void> dto = new ResponseDto<>();
        dto.setHttpStatus(exception.getHttpStatus());
        dto.setMessage(exception.getMessage());
        dto.setErrorCode(exception.getErrorCode());
        return dto;
    }

}
