package com.example.recipea.service.mapper;


import com.example.recipea.entity.Ingredient;
import com.example.recipea.service.dto.IngredientDto;
import org.mapstruct.Mapper;

/**
 * Mapper for convert entity <--->  IngredientDto
 */
@Mapper(componentModel = "spring")
public interface IngredientMapper extends EntityMapper<IngredientDto, Ingredient> {
}
