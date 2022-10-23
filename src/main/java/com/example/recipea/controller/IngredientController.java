package com.example.recipea.controller;

import com.example.recipea.service.dto.IngredientDto;
import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.service.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.net.URISyntaxException;

/**
 * @author Mahdi Sharifi
 */
@SecurityRequirement(name = "JWTtoken")
public interface IngredientController {

    @PostMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create a Ingredient. ",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Request.", content = @Content)
    })
    @Operation(summary = "Create a ingredient with Tags and ingredients, Use tags for add different label to the recipe.")
    ResponseEntity<ResponseDto<IngredientDto>> createIngredient(@Valid @RequestBody IngredientDto ingredientDto) throws URISyntaxException;

    @GetMapping("")
    @Operation(summary = "Get all ingredients.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success. Retrieve all ingredients.", content = @Content)})
    ResponseEntity<ResponseDto<IngredientDto>> getAllIngredients();

    @GetMapping("/{id}")
    @Operation(summary = "Get one ingredient by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success. Retrieve a ingredient.", content = @Content)})
    ResponseEntity<ResponseDto<IngredientDto>> getIngredient(@PathVariable Long id);
}



