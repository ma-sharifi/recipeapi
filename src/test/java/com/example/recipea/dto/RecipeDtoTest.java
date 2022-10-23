package com.example.recipea.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.recipea.service.dto.RecipeDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

class RecipeDtoTest {

    @Test
    void testDto() throws Exception {
        RecipeDto recipeDto1 = new RecipeDto();
//        recipeDto1.setId(new BigInteger(1+""));
        RecipeDto recipeDto2 = new RecipeDto();
        recipeDto2.setId(recipeDto1.getId());
        assertThat(recipeDto1).isEqualTo(recipeDto2);

//        recipeDTO1.setId(1L);
//        Assertions.assertThat(recipeDTO1).isNotEqualTo(recipeDTO2);
//        recipeDTO2.setId(recipeDTO1.getId());
//        Assertions.assertThat(recipeDTO1).isEqualTo(recipeDTO2);
//        recipeDTO2.setId(2L);
//        Assertions.assertThat(recipeDTO1).isNotEqualTo(recipeDTO2);
//        recipeDTO1.setId(null);
//        Assertions.assertThat(recipeDTO1).isNotEqualTo(recipeDTO2);
    }
}
