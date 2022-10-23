package com.example.recipea.controller;

import com.example.recipea.service.dto.IngredientDto;
import com.example.recipea.service.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.net.URISyntaxException;

/**
 * @author Mahdi Sharifi
 */

public interface IngredientController {

    @PostMapping("")
    ResponseEntity<ResponseDto<IngredientDto>> createIngredient(@Valid @RequestBody IngredientDto ingredientDto) throws URISyntaxException;

    @GetMapping("")
    ResponseEntity<ResponseDto<IngredientDto>> getAllIngredients();

    @GetMapping("/{id}")
    ResponseEntity<ResponseDto<IngredientDto>> getIngredient(@PathVariable Long id);
}
