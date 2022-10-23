package com.example.recipea.service;

import com.example.recipea.service.dto.IngredientDto;

import java.util.List;

/**
 * Service Interface for managing Ingredient.
 */
public interface IngredientService {

    IngredientDto save(IngredientDto IngredientDto);

    List<IngredientDto> findAll();

    IngredientDto findOne(Long id);

}
