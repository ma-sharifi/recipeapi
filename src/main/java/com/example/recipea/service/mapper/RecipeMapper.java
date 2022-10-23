package com.example.recipea.service.mapper;

import com.example.recipea.entity.Recipe;
import com.example.recipea.service.dto.CycleAvoidingMappingContext;
import com.example.recipea.service.dto.RecipeDto;
import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for convert Recipe <--->  RecipeDto
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipeMapper extends EntityCyclingMapper<RecipeDto, Recipe> {

    Recipe toEntity(RecipeDto dto, @Context CycleAvoidingMappingContext context);


    @InheritInverseConfiguration
    RecipeDto toDto(Recipe entity, @Context CycleAvoidingMappingContext context);

    RecipeDto toDto(Recipe entity);
}
