package com.example.recipea.dto;

import com.example.recipea.service.dto.IngredientDto;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IngredientDtoTest {



    @Test
    void testDto() throws Exception {
        IngredientDto IngredientDto1 = new IngredientDto();
        IngredientDto1.setId(1L);
        IngredientDto IngredientDto2 = new IngredientDto();
        IngredientDto2.setId(IngredientDto1.getId());
        assertThat(IngredientDto1).isEqualTo(IngredientDto2);

    }
}
