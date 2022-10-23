package com.example.recipea.service.impl;

import com.example.recipea.entity.Ingredient;
import com.example.recipea.entity.Recipe;
import com.example.recipea.exception.IngredientNotFoundException;
import com.example.recipea.exception.RecipeNotFoundException;
import com.example.recipea.repository.IngredientRepository;
import com.example.recipea.service.IngredientService;
import com.example.recipea.service.dto.CycleAvoidingMappingContext;
import com.example.recipea.service.dto.IngredientDto;
import com.example.recipea.service.mapper.IngredientMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Mahdi Sharifi
 */
@Service
@Transactional
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository repository;

    private final IngredientMapper mapper;

    public IngredientServiceImpl(IngredientRepository repository, IngredientMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public IngredientDto save(IngredientDto ingredientDto) {
        log.debug("Request to save/update Ingredient : {}", ingredientDto);
        Ingredient ingredient = mapper.toEntity(ingredientDto);
        ingredient = repository.save(ingredient);
        return mapper.toDto(ingredient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientDto> findAll() {
        log.debug("Request to get all Ingredients");
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public IngredientDto findOne(Long id) {
        log.debug("Request to get Ingredient : {}", id);
        Optional<Ingredient> ingredientOptional = repository.findById(id);
        return ingredientOptional.map(mapper::toDto).orElseThrow(() ->
                new IngredientNotFoundException(id+""));
    }

}
