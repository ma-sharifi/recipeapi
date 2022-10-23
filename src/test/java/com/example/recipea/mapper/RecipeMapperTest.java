package com.example.recipea.mapper;

import com.example.recipea.entity.Ingredient;
import com.example.recipea.service.dto.CycleAvoidingMappingContext;
import com.example.recipea.service.dto.IngredientDto;
import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.entity.Recipe;
import com.example.recipea.service.mapper.RecipeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RecipeMapperTest {

    @Autowired
    private RecipeMapper mapper;

    @Test
    void testMapEntityToDto() {
        Recipe  entity= new Recipe();
        entity.setId(1L);
        entity.setTitle("Chicken");
        RecipeDto dto = mapper.toDto(entity, new CycleAvoidingMappingContext());
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getTitle(), dto.getTitle());
    }
    @Test
    void testMapDtoToEntity() {
        IngredientDto IngredientDto= new IngredientDto();
        IngredientDto.setId(1L);
        IngredientDto.setTitle("Salt");

        RecipeDto dto = new RecipeDto();
        dto.setTitle("Chicken");
//        dto.setId(1L);
        dto.setIngredients(List.of(IngredientDto));

        Recipe entity = mapper.toEntity(dto , new CycleAvoidingMappingContext());


        assertThat( entity ).isNotNull();
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getTitle(), entity.getTitle());
        Set<Ingredient> ingredients = entity.getIngredients();
        assertThat( ingredients ).hasSize( 1 );
    }
}
