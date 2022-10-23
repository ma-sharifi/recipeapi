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

    List<RecipeDto> findByIngredientsAndInstructionAndServeAndVegetarian(String ingredient,
                                                                                String instruction, Boolean isveg, Integer serve, String username);

    RecipeDto save(RecipeDto recipeDto);

    RecipeDto update(RecipeDto recipeDto);

    Page<RecipeDto> findAll(Pageable pageable,String username);

    RecipeDto findOne(Long id,String username) throws RecipeNotFoundException;

    void deleteByIdAndUsername(Long id,String name);

}
