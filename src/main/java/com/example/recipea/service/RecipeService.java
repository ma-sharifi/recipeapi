package com.example.recipea.service;

import com.example.recipea.exception.RecipeNotFoundException;
import com.example.recipea.service.dto.RecipeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Recipe.
 */
public interface RecipeService {

    List<RecipeDto> findByIngredientsAndInstructionAndServeAndVegetarian(String ingredient, String tag,
                                                                                String instruction, Boolean isveg, Integer serve);

    RecipeDto save(RecipeDto recipeDto);

    RecipeDto update(RecipeDto recipeDto);

    Page<RecipeDto> findAll(Pageable pageable);

    RecipeDto findOne(Long id) throws RecipeNotFoundException;

    void delete(Long id);

}
