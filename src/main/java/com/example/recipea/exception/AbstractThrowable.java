package com.example.recipea.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 */

@AllArgsConstructor @Getter
public class AbstractThrowable extends RuntimeException {

    private final String message;//"Could not find recipe with id: "
    private final HttpStatus httpStatus; // NOT_FOUND
    private final int errorCode;
}
