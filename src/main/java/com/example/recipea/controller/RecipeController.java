package com.example.recipea.controller;

import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.service.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Mahdi Sharifi
 */
//Use just for simplicity of documents
public interface RecipeController {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of RecipeDto. " +
                    "recipes?tags=diet,-vegetarian&serve=4&instruction=fire&ingredients=rice,-oil",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Request.", content = @Content)})
    @Operation(summary = "Search one Recipe by tags,ingredients,serve,instruction.If you want to exclude something just add a minus before it. tags=diet,-vegetarian&ingredients=rice,-oil")
    ResponseEntity<ResponseDto<RecipeDto>>  searchRecipes(
            @Parameter(description = "How many peaople can serve this recipe.")
            @RequestParam(required = false, defaultValue = "0") Integer serve,
            @Parameter(description = "For include use just rice , for exclude use -rice. [ingredients=rice,salt,-oil]")
            @RequestParam(required = false, defaultValue = "") String ingredients,
            @Parameter(description = "Can search on the instruction of the recipe by keyword")
            @RequestParam(required = false, defaultValue = "") String instruction,
            @Parameter(description = "Can search on the isVegetarian of the recipe by keyword")
            @RequestParam(required = false) Boolean isVegetarian);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create a RecipeDto. ",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Request.", content = @Content)
    })
    @Operation(summary = "Create a Recipe with Tags and ingredients, Use tags for add different label to the recipe.")
    ResponseEntity<ResponseDto<RecipeDto>> createRecipe(@Valid @RequestBody RecipeDto recipeDto) throws URISyntaxException;

    @Operation(summary = "Get One Recipe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success. Retrieve a recipes.", content = @Content)})
    ResponseEntity<ResponseDto<RecipeDto>> getRecipe(@PathVariable Long id);


    @Operation(summary = "get all recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success. Retrieve all recipes. you can pass pagination parameters as query. ?sort=id,desc&page=0&size=2", content = @Content)})
    ResponseEntity<ResponseDto<RecipeDto>> getAllRecipes(@org.springdoc.api.annotations.ParameterObject Pageable pageable);

    @Operation(summary = "Update a recipe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid Request | The provided Id with RecipeDto.id is different", content = @Content),
            @ApiResponse(responseCode = "404", description = "The entity not found.", content = @Content),
            @ApiResponse(responseCode = "200", description = "Success. There is no content.", content = @Content)})
    ResponseEntity<ResponseDto<RecipeDto>>  updateRecipe(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody RecipeDto recipeDto) throws URISyntaxException;


    @Operation(summary = "Delete a Recipe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid Request.", content = @Content),
            @ApiResponse(responseCode = "404", description = "The entity not found.", content = @Content),
            @ApiResponse(responseCode = "204", description = "Success. There is no content.", content = @Content)})
    ResponseEntity<Void> deleteRecipe(@PathVariable Long id);

}
