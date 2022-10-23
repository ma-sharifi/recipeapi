package com.example.recipea.mapper;

import com.example.recipea.service.dto.CycleAvoidingMappingContext;
import com.example.recipea.service.dto.IngredientDto;
import com.example.recipea.entity.Ingredient;
import com.example.recipea.service.mapper.IngredientMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class IngredientMapperTest {

    @Autowired
    private IngredientMapper mapper;

    @Test
    void givenEntityToDto_whenMaps_thenCorrect() {
        Ingredient entity= new Ingredient();
        entity.setId(1L);
        entity.setTitle("Salt");
        IngredientDto dto = mapper.toDto(entity );
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getTitle(), dto.getTitle());
    }
    @Test
    void givenDtoToEntity_whenMaps_thenCorrect() {
        IngredientDto dto = new IngredientDto();
        dto.setTitle("Salt");
        dto.setId(1L);
        Ingredient entity = mapper.toEntity(dto);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getTitle(), entity.getTitle());
    }
}
