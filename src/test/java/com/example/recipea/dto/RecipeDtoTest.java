package com.example.recipea.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.recipea.service.dto.RecipeDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class RecipeDtoTest {

    @Test
    void testDto()  {
        RecipeDto recipeDto1 = new RecipeDto();
        recipeDto1.setId(1L);
        recipeDto1.setTitle("title");
        RecipeDto recipeDto2 = new RecipeDto();
        recipeDto2.setId(recipeDto1.getId());
        recipeDto2.setTitle(recipeDto1.getTitle());
        assertThat(recipeDto1).isEqualTo(recipeDto2);

    }
}
