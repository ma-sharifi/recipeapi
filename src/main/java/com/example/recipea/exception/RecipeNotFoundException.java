package com.example.recipea.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Mahdi Sharifi
 * When entity not found throw this exception
 */
@Getter
public class RecipeNotFoundException extends AbstractThrowable {

    public RecipeNotFoundException(String title) {
        super("Could not find the recipe: " + title, HttpStatus.NOT_FOUND, 4041);
    }

}
