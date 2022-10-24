package com.example.recipea.controller.impl;

import com.example.recipea.controller.RecipeController;
import com.example.recipea.exception.BadRequestException;
import com.example.recipea.security.AuthenticationFacade;
import com.example.recipea.service.RecipeService;
import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.service.dto.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

/**
 * REST controller for managing Recipe.
 */
@Tag(name = "recipe-controller for handling users recipe", description = "Get|Create|Search|Delete the recipe")
@RestController
@RequestMapping("/v1/recipes")
@Slf4j
public class RecipeControllerImpl implements RecipeController {

    private final RecipeService recipeService;

    private final AuthenticationFacade authenticationFacade;//For getting user's username

    public RecipeControllerImpl(RecipeService recipeService, AuthenticationFacade authenticationFacade) {
        this.recipeService = recipeService;
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * 1. Whether or not the dish is vegetarian: &isveg=true -> return all vegetarian ;&isveg=false -> return all none vegetarian
     * 2. The number of servings: &serve=4
     * 3. Specific ingredients (either include or exclude): for include-> &ingredient=salmon ; for exclude-> &ingredient=-salmon
     * 4. Text search within the instructions:  &instruction=oven
     * API should be able to handle the following search requests:
     * • All vegetarian recipes: ?isveg=true -> return all vegetarian
     * • Recipes that can serve 4 persons and have “potatoes” as an ingredient: ?serve=4&ingredient=potatoes
     * • Recipes without “salmon” as an ingredient that has “oven” in the instructions: ?ingredient=-salmon&instruction=oven
     */
    @GetMapping("/search")
    public ResponseEntity<ResponseDto<RecipeDto>> searchRecipes(
            @RequestParam(required = false) Integer serve,
            @RequestParam(required = false, defaultValue = "") String ingredient, //ingredient=rice salt -oil
            @RequestParam(required = false) String instruction,
            @RequestParam(required = false) Boolean isveg

    ) {
        log.debug("#searchRecipes is called: " + "serve = " + serve + ", ingredient = " + ingredient + ", instruction = " + instruction + ", isveg = " + isveg);
        String username = authenticationFacade.getAuthentication().getName();
        List<RecipeDto> recipeDtoList = recipeService.findByIngredientsAndInstructionAndServeAndVegetarian(ingredient, instruction, isveg, serve, username);
        ResponseDto<RecipeDto> responseDto = new ResponseDto<>(recipeDtoList);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto<RecipeDto>> getAllRecipes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get all Recipes");
        Page<RecipeDto> resultPage = recipeService.findAll(pageable, authenticationFacade.getAuthentication().getName());
        List<RecipeDto> resultRecipeList = resultPage.getContent();
        return ResponseEntity.ok(ResponseDto.<RecipeDto>builder().payload(resultRecipeList).build());
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto<RecipeDto>> createRecipe(@Valid @NotNull @RequestBody RecipeDto recipeDto) throws URISyntaxException {
        recipeDto.setUsername(authenticationFacade.getAuthentication().getName());
        RecipeDto result = recipeService.save(recipeDto);
        ResponseDto<RecipeDto> responseDto = ResponseDto.<RecipeDto>builder().payload(List.of(result)).httpStatus(HttpStatus.CREATED).build();
        return ResponseEntity
                .created(new URI("/v1/recipes/" + result.getId()))//Put the url of just added resource into Location HTTP header.
                .body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<RecipeDto>> updateRecipe(
            @NotNull(message = "id is mandatory") @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody RecipeDto recipeDto) {
        if (!Objects.equals(id, recipeDto.getId())) { //Provided Id , with ID of object must be the  same.
            throw new BadRequestException("Invalid Recipe ID! id in the path must be the same as the id of object! id path: " + id + " ;id object: " + recipeDto.getId());
        }
        recipeDto.setUsername(authenticationFacade.getAuthentication().getName()); // update the user who updated the recipe
        RecipeDto result = recipeService.update(recipeDto);
        ResponseDto<RecipeDto> responseDto = new ResponseDto<>(List.of(result));
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<RecipeDto>> getRecipe(@NotNull(message = "id is mandatory") @PathVariable Long id) {
        RecipeDto recipeDto = recipeService.findOne(id, authenticationFacade.getAuthentication().getName());
        ResponseDto<RecipeDto> responseDto = new ResponseDto<>(List.of(recipeDto));
        return ResponseEntity.status(responseDto.getHttpStatus()).body(responseDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        long effectedRow = recipeService.deleteByIdAndUsername(id, authenticationFacade.getAuthentication().getName());
        return ResponseEntity.status(effectedRow > 0 ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST).build();
    }

}
