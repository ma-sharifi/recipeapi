package com.example.recipea.controller.impl;


import com.example.recipea.controller.IngredientController;
import com.example.recipea.service.IngredientService;
import com.example.recipea.service.dto.IngredientDto;
import com.example.recipea.service.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Ingredient.
 */
@Tag(name = "ingredient-controller for handling ingredient", description = "Get|Create the ingredient")
@RestController
@RequestMapping("/v1/ingredients")
@Slf4j
public class IngredientControllerImpl implements IngredientController {

    private final IngredientService ingredientService;

    public IngredientControllerImpl(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * Create/Update a new ingredient.
     */
    @Override
    @PostMapping("")
    public ResponseEntity<ResponseDto<IngredientDto>> createIngredient(@Valid @RequestBody IngredientDto ingredientDto) throws URISyntaxException {
        log.debug("REST request to save Ingredient : {}", ingredientDto);
        IngredientDto result = ingredientService.save(ingredientDto);
        return ResponseEntity
                .created(new URI("/v1/ingredients/" + result.getId()))
                .body(ResponseDto.<IngredientDto>builder().httpStatus(HttpStatus.CREATED).payload(List.of(result)).build());
    }

    /**
     * get all the ingredients.
     */
    @Override
    @GetMapping("")
    public ResponseEntity<ResponseDto<IngredientDto>> getAllIngredients() {
        log.debug("REST request to get all Ingredients");
        List<IngredientDto> resultList = ingredientService.findAll();
        return ResponseEntity.ok(ResponseDto.<IngredientDto>builder().payload(resultList).build());
    }

    /**
     * get the "id" ingredient.
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<IngredientDto>> getIngredient(@PathVariable Long id) {
        log.debug("REST request to get Ingredient : {}", id);
        IngredientDto ingredientDto = ingredientService.findOne(id);
        return ResponseEntity.ok(ResponseDto.<IngredientDto>builder().payload(List.of(ingredientDto)).build());
    }

}
