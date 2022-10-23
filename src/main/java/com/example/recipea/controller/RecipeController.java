package com.example.recipea.controller;

import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.service.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@SecurityRequirement(name = "JWTtoken")
public interface RecipeController {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of RecipeDto. " +
                    "recipes?serve=4&instruction=oven&ingredient=-salmon&isveg=false",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Request.", content = @Content)})
    @Operation(summary = "Search one Recipe by ingredient,serve,instruction and isveg.If you want to exclude something just add a minus before it. &ingredient=-salmon &ingredient=oil")
    ResponseEntity<ResponseDto<RecipeDto>>  searchRecipes(
            @Parameter(description = "How many people can serve this recipe." ,example = "4")
            @RequestParam(required = false) Integer serve,
            @Parameter(description = "For include use just rice , for exclude use -rice. ingredient=-salmon or ingredient=potatoes" ,example = "-salmon")
            @RequestParam(required = false, defaultValue = "") String ingredient,
            @Parameter(description = "Can search on the instruction of the recipe by keyword. for example instruction=oven" ,example = "oven")
            @RequestParam(required = false) String instruction,
            @Parameter(description = "Can search on the isveg of the recipe by keyword",example = "true")
            @RequestParam(required = false ) Boolean isveg);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create a RecipeDto. ",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Request.", content = @Content)
    })
    @Operation(summary = "Create a Recipe with list of ingredients.List of ingredients must be provided by id at ingredients field.")
    ResponseEntity<ResponseDto<RecipeDto>> createRecipe(@Valid @RequestBody RecipeDto recipeDto) throws URISyntaxException;

    @Operation(summary = "Get One Recipe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success. Retrieve a recipes.", content = @Content)})
    ResponseEntity<ResponseDto<RecipeDto>> getRecipe(
            @Parameter(description = "The id we will search base on",example = "4")
            @PathVariable Long id);


    @Operation(summary = "get all recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success. Retrieve all recipes. you can pass pagination parameters as query. ?sort=id,desc&page=0&size=2", content = @Content)})
    ResponseEntity<ResponseDto<RecipeDto>> getAllRecipes(
            @Parameter(description = "Pagination parameters. Use sort for sorting. Page=0 means show me the page 0. size=2 means there are two items on per pages",example = "sort=id,desc&page=0&size=2")
            @org.springdoc.api.annotations.ParameterObject Pageable pageable);

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
