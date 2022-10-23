package com.example.recipea.service.mapper;

import com.example.recipea.service.dto.CycleAvoidingMappingContext;
import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;

/**
 * Contract for a generic dto to entity mapper.
 *
 * @param <D> - DTO type parameter.
 * @param <E> - Entity type parameter.
 */

public interface EntityCyclingMapper<D, E> {
    E toEntity(D dto, @Context CycleAvoidingMappingContext context);

    @InheritInverseConfiguration
    D toDto(E entity, @Context CycleAvoidingMappingContext context);

}
