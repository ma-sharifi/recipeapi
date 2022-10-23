package com.example.recipea.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 * When Movie not found throw this exception
 */
@Getter
public class IngredientNotFoundException extends AbstractThrowable {

    public IngredientNotFoundException(String title) {
        super("Could not find the ingredient: " + title, HttpStatus.NOT_FOUND, 4042);
    }

}
