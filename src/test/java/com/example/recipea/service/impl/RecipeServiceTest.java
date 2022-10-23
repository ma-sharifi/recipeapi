package com.example.recipea.service.impl;

import com.example.recipea.entity.Ingredient;
import com.example.recipea.entity.Recipe;
import com.example.recipea.exception.BadRequestException;
import com.example.recipea.exception.RecipeNotFoundException;
import com.example.recipea.repository.RecipeRepository;
import com.example.recipea.service.RecipeService;
import com.example.recipea.service.dto.IngredientDto;
import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.service.dto.ResponseDto;
import com.example.recipea.service.mapper.RecipeMapper;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Mahdi Sharifi
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class RecipeServiceTest {

    private static final String DEFAULT_TITLE = "Sushi";
    private static final String UPDATED_TITLE = "updated Sushi";

    private static final String DEFAULT_INSTRUCTION = "Sushi is a Japanese dish of prepared vinegared rice, usually with some sugar and salt, accompanied by a variety of ingredients, such as seafood, often raw, and vegetables.";
    private static final String UPDATED_INSTRUCTION = "Updated Sushi is a Japanese dish of prepared vinegared rice, usually with some sugar and salt, accompanied by a variety of ingredients, such as seafood, often raw, and vegetables. ";

    private static final Integer DEFAULT_SERVE = 1;
    private static final Integer UPDATED_SERVE = 2;

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeMapper mapper;

    private Recipe recipe;
    private final String username="mahdi";
    private RecipeDto recipeDtoActual;

    public static Recipe createEntity() {
        return new Recipe(DEFAULT_TITLE+"-"+ ThreadLocalRandom.current().nextInt(1000),DEFAULT_INSTRUCTION,DEFAULT_SERVE,"mahdi");
    }

    @BeforeEach
    public void initTest() {
        recipe = createEntity();
    }

    @Test
    void shouldReturnRecipeNotFoundException_whenRecipeIsNotFound() { //TODO
        RecipeNotFoundException exception = assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.findOne(10000L,"mahdi");
        });
        assertTrue(exception.getMessage().contains("Could not find the recipe"));

    }

    @Test
    void shouldReturnBadRequestException_whenRecipeIsNotFound() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            recipeService.update(mapper.toDto(recipe));
        });
        assertTrue(exception.getMessage().contains("Bad request! Something wrong in client request"));
    }


    @Test
    void shouldReturnRecipeDto_whenSaveIsCalled() {

        recipeDtoActual = recipeService.save(mapper.toDto(recipe));
        assertNotNull(recipeDtoActual);
        assertNotNull(recipeDtoActual.getId());
        assertEquals(recipe.getTitle(), recipeDtoActual.getTitle());
        assertEquals(DEFAULT_INSTRUCTION, recipeDtoActual.getInstruction());
        assertEquals(DEFAULT_SERVE, recipeDtoActual.getServe());
    }

    @Test
    @Transactional
    void shouldUpdateRecipe_whenUpdateIsCalled() {
        recipeDtoActual = recipeService.save(mapper.toDto(recipe));
        assertNotNull(recipeDtoActual);
        assertNotNull(recipeDtoActual.getId());
        assertEquals(recipe.getTitle(), recipeDtoActual.getTitle());
        assertEquals(DEFAULT_INSTRUCTION, recipeDtoActual.getInstruction());
        assertEquals(DEFAULT_SERVE, recipeDtoActual.getServe());

        recipeDtoActual.setTitle(UPDATED_TITLE);
        recipeDtoActual.setInstruction(UPDATED_INSTRUCTION);
        recipeDtoActual.setServe(UPDATED_SERVE);

        recipeDtoActual = recipeService.update(recipeDtoActual);

        assertNotNull(recipeDtoActual);
        assertNotNull(recipeDtoActual.getId());
        assertEquals(UPDATED_TITLE, recipeDtoActual.getTitle());
        assertEquals(UPDATED_INSTRUCTION, recipeDtoActual.getInstruction());
        assertEquals(UPDATED_SERVE, recipeDtoActual.getServe());

    }

    @Test
    @Transactional
    void shouldDeleteRecipe_whenDeleteIsCalled() {
        recipeDtoActual = recipeService.save(mapper.toDto(recipe));
        recipeService.deleteByIdAndUsername(recipeDtoActual.getId(),username);

        RecipeNotFoundException exception= assertThrows(RecipeNotFoundException.class , ()->{
            recipeService.findOne(recipeDtoActual.getId(),"mahdi") ;
        });
        assertTrue(exception.getMessage().contains("Could not find the recipe"));

    }

    @Test
    @Transactional
    void shouldThrowEmptyResultDataAccessException_whenDeleteIsCalled2Times() {
        recipeDtoActual = recipeService.save(mapper.toDto(recipe));
        recipeService.deleteByIdAndUsername(recipeDtoActual.getId(),username);

        RecipeNotFoundException exception= assertThrows(RecipeNotFoundException.class , ()->{
            recipeService.deleteByIdAndUsername(recipeDtoActual.getId(),username);
        });
        assertTrue(exception.getMessage().contains("Could not find the recipe"));
    }

    @Test
    @Transactional//Mandatory
    void shouldFindOneRecipe_whenFindOneIsCalled() {
        recipeDtoActual = recipeService.save(mapper.toDto(recipe));
        assertNotNull(recipeDtoActual);
        assertNotNull(recipeDtoActual.getId());
        assertEquals(recipe.getTitle(), recipeDtoActual.getTitle());
        assertEquals(DEFAULT_INSTRUCTION, recipeDtoActual.getInstruction());
        assertEquals(DEFAULT_SERVE, recipeDtoActual.getServe());

        RecipeDto recipeDtoExpected= recipeService.findOne(recipeDtoActual.getId(),"mahdi");

        assertNotNull(recipeDtoExpected);
        assertNotNull(recipeDtoExpected.getId());
        assertEquals(recipeDtoExpected.getTitle(), recipeDtoActual.getTitle());
        assertEquals(recipeDtoExpected.getInstruction(), recipeDtoActual.getInstruction());
        assertEquals(recipeDtoExpected.getServe(), recipeDtoActual.getServe());
    }

//    @Test
//    @Transactional
//    void shouldFindAllRecipe_whenFindAllIsCalled() { //TODO
////        long databaseSizeBeforeCreate= recipeService.;
//        RecipeDto recipeDtoActual1 = recipeService.save(mapper.toDto(new Recipe("Sushi1","Sushi is a Japanese dish1",1)));
//        RecipeDto recipeDtoActual2 = recipeService.save(mapper.toDto(new Recipe("Sushi2","Sushi is a Japanese dish2",2)));
//        RecipeDto recipeDtoActual3 = recipeService.save(mapper.toDto(new Recipe("Sushi3","Sushi is a Japanese dish3",3)));
//
//        List<RecipeDto> recipeListExpected = recipeService.findAll(PageRequest.of(0, 100),username).getContent();
//
//        long databaseSizeAfterCreate= recipeService.findAll(PageRequest.of(0, 100),username).stream().count();
//
//        assertEquals(databaseSizeAfterCreate,databaseSizeBeforeCreate + 3);
//    }

}