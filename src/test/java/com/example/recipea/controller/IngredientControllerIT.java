package com.example.recipea.controller;

import com.example.recipea.IntegrationTest;
import com.example.recipea.entity.Ingredient;
import com.example.recipea.exception.IngredientNotFoundException;
import com.example.recipea.repository.IngredientRepository;
import com.example.recipea.service.dto.IngredientDto;
import com.example.recipea.service.mapper.IngredientMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the Ingredient Endpoint REST controller.
 */

@IntegrationTest
class IngredientControllerIT {

    private static final String DEFAULT_TITLE = "oil";
    private static final String UPDATED_TITLE = "updated oil";


    private static final String ENTITY_API_URL = "/v1/ingredients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final String JWT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2Mzg1OTQzLCJleHAiOjE2Njg5ODE1NDN9.v4ghT5iLyItNHJ70062INiUSbHkgYx1LvTcDwWXV1_mTS0RZ1IlAgQB1BaRUm7PB9Cb4B5eO1lvWq--CgNq7rQ";

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private IngredientMapper ingredientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mockMvc;

    private Ingredient ingredient;

    int randomInt= 1;

    /**
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingredient createEntity(int random) {

        return new Ingredient(DEFAULT_TITLE+"-"+random);
    }

    @BeforeEach
    public void initTest() {
        randomInt= ThreadLocalRandom.current().nextInt(10000);
        ingredient = createEntity(randomInt);
    }

    @Test
    void shouldCreateIngredient_whenCreateIngredientIsCalled() throws Exception {
        long databaseSizeBeforeCreate = ingredientRepository.count();
        // Create the Ingredient
        IngredientDto ingredientDto = ingredientMapper.toDto(ingredient);
        mockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", JWT)
                    .content(new Gson().toJson(ingredientDto)))
            .andExpect(status().isCreated());

        long databaseSizeAfterCreate = ingredientRepository.count();
        // Validate the Ingredient in the database
        assertThat(databaseSizeAfterCreate).isEqualTo(databaseSizeBeforeCreate + 1);
    }
    @Test
    void getIngredient() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get the ingredient
        mockMvc
                .perform(get(ENTITY_API_URL_ID, ingredient.getId()).header("Authorization", JWT))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowRecipeNotFoundException_whenNonExistingIngredientIsCalled() throws Exception {
        //Get non-existing ingredient
        mockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)
                        .header("Authorization", JWT))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(4042))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IngredientNotFoundException) );
    }
    @Test
    void shouldThrowAccessDeniedException_whenJwtIsNotExistIsCalled() throws Exception {
        //Get non-existing ingredient
        mockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE))
                .andExpect(status().isForbidden());
    }

//    @Test
//    @Transactional
//    void checkTitleIsRequired() throws Exception {
//        int databaseSizeBeforeTest = ingredientRepository.findAll().size();
//        // set the field null
//        ingredient.setTitle(null);
//
//        // Create the Ingredient, which fails.
//        IngredientDto ingredientDto = ingredientMapper.toDto(ingredient);
//
//        mockMvc
//                .perform(post(ENTITY_API_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", JWT)
//                        .content(new Gson().toJson(ingredientDto)))
//                .andExpect(status().isBadRequest());
//
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        assertThat(ingredientList).hasSize(databaseSizeBeforeTest);
//    }

    @Test
    @Transactional
    void getAllIngredients() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        System.out.println("#ingredient: "+ingredient);

        // Get all the ingredientList
        mockMvc
                .perform(get(ENTITY_API_URL ).header("Authorization", JWT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.payload[*].id").value(hasItem(ingredient.getId().intValue())))
                .andExpect(jsonPath("$.payload[*].title").value(hasItem(DEFAULT_TITLE)));
    }
}
