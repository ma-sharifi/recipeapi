package com.example.recipea.controller;

import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.service.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.net.URISyntaxException;

/**
 * @author Mahdi Sharifi
 */
//Use just for simplicity of documents
public interface RecipeController {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of RecipeDto. " +
                    "recipes?tags=diet,-vegetarian&serve=4&instruction=fire&ingredients=rice,-oil",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Request.", content = @Content),
            @ApiResponse(responseCode = "204", description = "There is no content", content = @Content)})
    @Operation(summary = "Search one Recipe by tags,ingredients,serve,instruction.If you want to exclude something just add a minus before it. tags=diet,-vegetarian&ingredients=rice,-oil")
    ResponseEntity<ResponseDto<RecipeDto>>  searchRecipes(
            @Parameter(description = "How many peaople can serve this recipe.")
            @RequestParam(required = false, defaultValue = "0") Integer serve,
            @Parameter(description = "For include use just rice , for exclude use -rice. [ingredients=rice,salt,-oil]")
            @RequestParam(required = false, defaultValue = "") String ingredients,
            @Parameter(description = "For including use just Vegetarian and Gluten Free, for exclude use -Diet. [tags=Gluten Free,Vegetarian,-Diet]")
            @RequestParam(required = false,defaultValue = "") String tags, //
            @Parameter(description = "Can search on the instruction of the recipe by keyword")
            @RequestParam(required = false, defaultValue = "") String instruction,
            @Parameter(description = "Can search on the isVegetarian of the recipe by keyword")
            @RequestParam(required = false) Boolean isVegetarian
    );

    @Operation(summary = "Update a recipe by id")
    ResponseEntity<ResponseDto<RecipeDto>>  updateRecipe(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody RecipeDto recipeDto) throws URISyntaxException;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create a RecipeDto. ",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Request.", content = @Content)
    })
    @Operation(summary = "Create a Recipe with Tags and ingredients, Use tags for add different label to the recipe.")
    ResponseEntity<ResponseDto<RecipeDto>> createRecipe(@Valid @RequestBody RecipeDto recipeDto) throws URISyntaxException;

    @Operation(summary = "Get One Recipe by id")
    ResponseEntity<ResponseDto<RecipeDto>> getRecipe(@PathVariable Long id);

    @Operation(summary = "Delete a Recipe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Invalid Request.", content = @Content),
            @ApiResponse(responseCode = "204", description = "There is no content", content = @Content)})
    ResponseEntity<Void> deleteRecipe(@PathVariable Long id);

}
