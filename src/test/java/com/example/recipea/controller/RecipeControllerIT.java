package com.example.recipea.controller;

import com.example.recipea.IntegrationTest;
import com.example.recipea.entity.Ingredient;
import com.example.recipea.entity.Recipe;
import com.example.recipea.exception.BadRequestException;
import com.example.recipea.exception.RecipeNotFoundException;
import com.example.recipea.repository.IngredientRepository;
import com.example.recipea.repository.RecipeRepository;
import com.example.recipea.service.RecipeService;
import com.example.recipea.service.dto.CycleAvoidingMappingContext;
import com.example.recipea.service.dto.IngredientDto;
import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.service.dto.ResponseDto;
import com.example.recipea.service.mapper.RecipeMapper;
import com.example.recipea.util.LocalDateTimeDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.recipea.util.ConvertorUtil.toResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the Recipe Endpoint REST controller.
 */
@IntegrationTest
class RecipeControllerIT {

    private static final String DEFAULT_TITLE = "Sushi";
    private static final String UPDATED_TITLE = "updated Sushi";

    private static final String DEFAULT_INSTRUCTION = "Sushi is a Japanese dish of prepared vinegared rice, usually with some sugar and salt, accompanied by a variety of ingredients, such as seafood, often raw, and vegetables.";
    private static final String UPDATED_INSTRUCTION = "Updated Sushi is a Japanese dish of prepared vinegared rice, usually with some sugar and salt, accompanied by a variety of ingredients, such as seafood, often raw, and vegetables. ";

    private static final Integer DEFAULT_SERVE = 1;
    private static final Integer UPDATED_SERVE = 2;

    private static final String ENTITY_API_URL = "/v1/recipes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final String JWT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2Mzg1OTQzLCJleHAiOjE2Njg5ODE1NDN9.v4ghT5iLyItNHJ70062INiUSbHkgYx1LvTcDwWXV1_mTS0RZ1IlAgQB1BaRUm7PB9Cb4B5eO1lvWq--CgNq7rQ";
    Predicate<IngredientDto> isSalmon = i -> i.getTitle().equals("salmon");
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private RecipeMapper recipeMapper;
    @Autowired
    private MockMvc mockMvc;
    private Recipe recipe;
    int randomInt= 1;


    public static Recipe createEntity() {
        return new Recipe(DEFAULT_TITLE, DEFAULT_INSTRUCTION, DEFAULT_SERVE);
    }

    @BeforeEach
    public void initTest() {
        randomInt= ThreadLocalRandom.current().nextInt(10000);
        recipe = createEntity();
    }

    public void saveData() {
//        Ingredient ingredientOil = new Ingredient("oil");
//        ingredientRepository.save(ingredientOil);
//        Ingredient ingredientSalmon = new Ingredient("salmon");
//        ingredientRepository.save(ingredientSalmon);
//
//        Ingredient ingredientPotato = new Ingredient("potatoes");
//        ingredientRepository.save(ingredientPotato);
//
//        Recipe recipeSalmonKabab = new Recipe();
//        recipeSalmonKabab.setTitle("Salmon Kabab2");
//        recipeSalmonKabab.setServe(1);
//        recipeSalmonKabab.addIngredient(ingredientOil);
//        recipeSalmonKabab.addIngredient(ingredientSalmon);
//        recipeSalmonKabab.setInstruction("This kabab cooked by stove");
//        recipeSalmonKabab.setUsername("mahdi");
//
//        recipeRepository.save(recipeSalmonKabab);
//        //----------------
//        Recipe recipeRice = new Recipe("French fries2", "Cook potato on oven", 4);
//        recipeRice.setVegetarian(true);
//        recipeRice.addIngredient(ingredientPotato);
//        recipeRice.addIngredient(ingredientOil);
//        recipeRice.setUsername("mahdi");
//
//        recipeRepository.save(recipeRice);
    }

    /**
     * In Integration test you must test every dependency. Becasue of these dependencies it takes more than other test types.
     */

    @Test
    @Transactional
    void shouldReturnAllVegetarian_whenIsVegIsTrue() throws Exception {
        // Initialize the database
        saveData();
        // run query, we expect get the same result from endpoint.
        List<RecipeDto> listSearched = recipeService
                .findByIngredientsAndInstructionAndServeAndVegetarian(null , null, null, true, null);

        // Get all the recipeList
        MockHttpServletResponse responseMockMvc= mockMvc
                .perform(get(ENTITY_API_URL + "/search?isveg=true").header("Authorization", JWT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.payload[*].vegetarian",hasItems(true)))
                .andReturn().getResponse();

        // then convert json string to object ResponseDto<RecipeDto>
        ResponseDto<RecipeDto> responseDto = toResponseDto(responseMockMvc.getContentAsString());
        // The result of repository must be equal with result of the endpoint
        assertEquals(listSearched, responseDto.getPayload());

        long countNonVeg = responseDto.getPayload().stream().filter(RecipeDto::getVegetarian).count();
        assertEquals(countNonVeg, responseDto.getPayload().size());
    }

    @Test
    @Transactional
    void shouldReturnNonVegetarian_whenIsVegIsFalse() throws Exception {
        // Initialize the database
        saveData();
        // run query, we expect get the same result from endpoint.
        List<RecipeDto> listSearched = recipeService
                .findByIngredientsAndInstructionAndServeAndVegetarian(null , null, null, false, null);

        // Get all the recipeList
        MockHttpServletResponse responseMockMvc = mockMvc
                .perform(get(ENTITY_API_URL + "/search?isveg=false").header("Authorization", JWT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.payload[*].vegetarian",hasItems(false)))
                .andReturn().getResponse();

        // then convert json string to object ResponseDto<RecipeDto>
        ResponseDto<RecipeDto> responseDto = toResponseDto(responseMockMvc.getContentAsString());
        // The result of repository must be equal with result of the endpoint
        assertEquals(listSearched, responseDto.getPayload());

        long countNonVeg = responseDto.getPayload().stream().filter(recipe -> !recipe.getVegetarian()).count();
        assertEquals(countNonVeg, responseDto.getPayload().size());
    }

    /**
     * Recipes that can serve 4 persons and have “potatoes” as an ingredient: ?serve=4&ingredient=potatoes
     */
    @Test
    @Transactional
    void shouldReturnRecipesForServe4PersonsAndPotatoesIngredient_whenServe4AndIngredientPotatoesIsCalled() throws Exception {
        // Initialize the database
        saveData();
        // run query, we expect get the same result from endpoint.
        List<RecipeDto> listSearched = recipeService.findByIngredientsAndInstructionAndServeAndVegetarian("potatoes", null, null, null, 4);
        // Get all the recipeList
        MockHttpServletResponse responseMockMvc = mockMvc
                .perform(get(ENTITY_API_URL + "/search?serve=4&ingredient=potatoes").header("Authorization", JWT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").exists())
                .andExpect(jsonPath("$.payload[*].ingredients[*].title", hasItems("potatoes")))//OK
                .andExpect(jsonPath("$.payload[*].serve", hasItems(4)))
                .andReturn().getResponse();

        // then convert json string to object ResponseDto<RecipeDto>
        ResponseDto<RecipeDto> responseDto = toResponseDto(responseMockMvc.getContentAsString());
        // The result of repository must be equal with result of the endpoint
        assertEquals(listSearched, responseDto.getPayload());

        //Each recipe must have potatoes
        long sizePotatoRecipe= responseDto.getPayload().stream()
                .filter(recipe -> recipe.getIngredients().stream().anyMatch(ingredientDto -> ingredientDto.getTitle().contains("potatoes"))).count();
        assertEquals(sizePotatoRecipe,responseDto.getPayload().size());
        // all recipes must have oven
        long countServe = responseDto.getPayload().stream().filter(recipe -> recipe.getServe()==4).count();
        assertEquals(countServe, responseDto.getPayload().size());

    }

    /**
     *  Recipes without “salmon” as an ingredient that has “oven” in the instructions: ?ingredient=-salmon&instruction=oven
     */
    @Test
    @Transactional
    void shouldReturnRecipesWithoutSalmonIngredientWithOvenInstruction_whenIngredientMinusSalmonAndInstructionStoveIsCalled() throws Exception {

        // Initialize the database
        saveData();
        // run query, we expect get the same result from endpoint.
        List<RecipeDto> listSearched = recipeService
                .findByIngredientsAndInstructionAndServeAndVegetarian("-salmon", null, "oven", null, null);

        // search all the recipeList from endpoint
        MockHttpServletResponse responseMockMvc = mockMvc
                .perform(get(ENTITY_API_URL + "/search?ingredient=-salmon&instruction=oven").header("Authorization", JWT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").exists())
                .andReturn().getResponse();

        // then convert json string to object ResponseDto<RecipeDto>
        ResponseDto<RecipeDto> responseDto = toResponseDto(responseMockMvc.getContentAsString());
        // The result of repository must be equal with result of the endpoint
        assertEquals(listSearched, responseDto.getPayload());

        //No recipe must have salmon
        long sizeNonSalmonRecipe= responseDto.getPayload().stream()
                .filter(recipe -> recipe.getIngredients().stream().noneMatch(ingredientDto -> ingredientDto.getTitle().contains("salmon"))).count();
        assertEquals(sizeNonSalmonRecipe,responseDto.getPayload().size());
        // all recipes must have oven
        long countOven = responseDto.getPayload().stream().filter(recipe -> recipe.getInstruction().contains("oven")).count();
        assertEquals(countOven, responseDto.getPayload().size());
        //https://reflectoring.io/assertj-lists/
    }



    @Test
    void shouldGetNotFound_whenPathIsNotExist() throws Exception {
        mockMvc.perform(
                        get(ENTITY_API_URL + "/not/existing-path")
                                .header("Authorization", JWT)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldCreateRecipe_whenCreateRecipeIsCalled() throws Exception {
        long databaseSizeBeforeCreate = recipeRepository.count(); //get the number of our recipe in our database
        // Create the Recipe
        recipe.setTitle("Title for test create recipe in IT");
        RecipeDto recipeDto = recipeMapper.toDto(recipe, new CycleAvoidingMappingContext());
        //add new recipe into database via our endpoint
        mockMvc.perform(
                        post(ENTITY_API_URL)
                                .header("Authorization", JWT)
                                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(recipeDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].title").value("Title for test create recipe in IT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].instruction").value(DEFAULT_INSTRUCTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].serve").value(DEFAULT_SERVE));
        // Validate the Recipe in the database
        long databaseSizeAfterCreate = recipeRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1); //test if the size of our save data increased by 1

        // test if we save the same title and username in dto from our endpoint, we get bad request with error code 4009?
        //shouldJdbcSQLIntegrityConstraintViolationExceptionWithDuplicateRecipeTitleAndUsername_whenCreateRecipeIsCalled
        mockMvc.perform(
                        post(ENTITY_API_URL)
                                .header("Authorization", JWT)
                                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(recipeDto)))//Duplicate Title and Username
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(4005));

    }

    @Test
    @Transactional
    void shouldUpdateRecipe_whenUpdateRecipeIsCalled() throws Exception {
        long databaseSizeBeforeCreate = recipeRepository.count();
        // Create the Recipe
        recipe.setTitle("Create Title for recipe");
        RecipeDto recipeDto = recipeMapper.toDto(recipe, new CycleAvoidingMappingContext());

        MvcResult mvcResult = mockMvc.perform(
                        post(ENTITY_API_URL)
                                .header("Authorization", JWT)
                                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(recipeDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].title").value("Create Title for recipe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].instruction").value(DEFAULT_INSTRUCTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].serve").value(DEFAULT_SERVE)).andReturn();
        // Validate the Recipe in the database
        long databaseSizeAfterCreate = recipeRepository.count();
        assertEquals(databaseSizeAfterCreate, databaseSizeBeforeCreate + 1);

        String urlSavedRecipe = mvcResult.getResponse().getHeader("Location");// Extract the url of created object.
        long idSavedRecipe = Long.parseLong(urlSavedRecipe.substring(urlSavedRecipe.lastIndexOf("/") + 1)); // Extract the id of created  object

        Recipe recipeLoadedFromDb = recipeRepository.findById(idSavedRecipe).get();//Read the entity from database
        assertEquals("Create Title for recipe", recipeLoadedFromDb.getTitle()); // Check Title with Database
        assertEquals(DEFAULT_INSTRUCTION, recipeLoadedFromDb.getInstruction()); // Check Instruction with Database
        assertEquals(DEFAULT_SERVE, recipeLoadedFromDb.getServe()); // Check Serve with Database

        recipeLoadedFromDb.setTitle("Update Title for recipe");
        recipeLoadedFromDb.setInstruction(UPDATED_INSTRUCTION);
        recipeLoadedFromDb.setServe(UPDATED_SERVE);

        mockMvc.perform(
                        put(urlSavedRecipe)
                                .header("Authorization", JWT)
                                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(recipeMapper.toDto(recipeLoadedFromDb))))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].title").value("Update Title for recipe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].instruction").value(UPDATED_INSTRUCTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].serve").value(UPDATED_SERVE));

        Recipe updatedRecipeLoadedFromDb = recipeRepository.findById(idSavedRecipe).get();//Read the entity from database
        assertEquals(recipeLoadedFromDb.getId(), updatedRecipeLoadedFromDb.getId()); // Check id with Database
        assertEquals("Update Title for recipe", updatedRecipeLoadedFromDb.getTitle()); // Check Title with Database
        assertEquals(UPDATED_INSTRUCTION, updatedRecipeLoadedFromDb.getInstruction()); // Check Instruction with Database
        assertEquals(UPDATED_SERVE, updatedRecipeLoadedFromDb.getServe()); // Check Serve with Database
    }

    @Test
    void shouldGetRecipe_whenFindByIdIsCalled() throws Exception {
        recipe.setTitle("Test Get recipe by id");
        recipeRepository.save(recipe); // save recipe into database and will read it from endpoint

        //Read the data of saved recipe from endpoint then test them
        mockMvc.perform(get(ENTITY_API_URL + "/" + recipe.getId())
                        .header("Authorization", JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error_code").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].title").value("Test Get recipe by id"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].instruction").value(DEFAULT_INSTRUCTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload[0].serve").value(DEFAULT_SERVE));
    }

    @Test
    void shouldDeleteById_whenDeleteIsCalled() throws Exception {
        recipe.setTitle("Test delete recipe by id");
        recipeRepository.save(recipe); // save recipe into database and will read it from endpoint

        //Read the data of saved recipe from endpoint then test them
        mockMvc.perform(delete(ENTITY_API_URL + "/" + recipe.getId())
                        .header("Authorization", JWT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<Recipe> recipeLoadFromDbOptional = recipeRepository.findById(recipe.getId()); // read recipe from database. t must be deleted
        assertFalse(recipeLoadFromDbOptional.isPresent());
    }


    @Test
    @Transactional
    void getAllRecipes() throws Exception {
        // Initialize the database
        recipe.setTitle("Sushi for find all test");
        recipeRepository.save(recipe); // Save entity, we expect get the same object from out endpoint.

        // Get all the recipeList
        mockMvc
                .perform(get(ENTITY_API_URL).header("Authorization", JWT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.payload[*].id").value(hasItem(recipe.getId().intValue())))
                .andExpect(jsonPath("$.payload[*].title").value(hasItem("Sushi for find all test")))
                .andExpect(jsonPath("$.payload[*].instruction").value(hasItem(DEFAULT_INSTRUCTION)))
                .andExpect(jsonPath("$.payload[*].serve").value(hasItem(DEFAULT_SERVE)));

    }

    @Test
    void shouldThrowException_whenCreateRecipeWithExistingIdIsCalled() throws Exception {
        // Create the Recipe with an existing ID
        recipe.setId(1L);
        RecipeDto recipeDto = recipeMapper.toDto(recipe);

        long databaseSizeBeforeCreate = recipeRepository.count();

        // An entity with an existing ID cannot be created, so this API call must fail
        mockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", JWT)
                        .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(recipeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(4001))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException));

        // Validate the Recipe in the database
        long databaseSizeAfterCreate = recipeRepository.count();
        assertThat(databaseSizeBeforeCreate).isEqualTo(databaseSizeAfterCreate);
    }


    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeCreate = recipeRepository.count();
        // set the field null
        recipe.setTitle(null);

        // Create the Recipe, which fails.
        RecipeDto recipeDto = recipeMapper.toDto(recipe);

        mockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", JWT)
                        .content(new Gson().toJson(recipeDto)))
                .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        long databaseSizeAfterCreate = recipeRepository.count();
        assertThat(databaseSizeBeforeCreate).isEqualTo(databaseSizeAfterCreate);
    }

    @Test
    @Transactional
    void checkInstructionIsRequired() throws Exception {
        long databaseSizeBeforeCreate = recipeRepository.count();
        // set the field null
        recipe.setInstruction(null);

        // Create the Recipe, which fails.
        RecipeDto recipeDto = recipeMapper.toDto(recipe);

        mockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", JWT)
                        .content(new Gson().toJson(recipeDto)))
                .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        long databaseSizeAfterCreate = recipeRepository.count();
        assertThat(databaseSizeBeforeCreate).isEqualTo(databaseSizeAfterCreate);
    }

    @Test
    @Transactional
    void checkServeIsRequired() throws Exception {
        long databaseSizeBeforeCreate = recipeRepository.count();
        // set the field null
        recipe.setServe(null);

        // Create the Recipe, which fails.
        RecipeDto recipeDto = recipeMapper.toDto(recipe);

        mockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", JWT)
                        .content(new Gson().toJson(recipeDto)))
                .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        long databaseSizeAfterCreate = recipeRepository.count();
        assertThat(databaseSizeBeforeCreate).isEqualTo(databaseSizeAfterCreate);
    }

    @Test
    void shouldThrowRecipeNotFoundException_whenNonExistingRecipeIsCalled() throws Exception {
        // Get the non-existing recipe
        mockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)
                        .header("Authorization", JWT))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(4041))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecipeNotFoundException));
    }

    @Test
    void shouldThrowAccessDeniedException_whenJwtIsNotExistIsCalled() throws Exception {
        //Get non-existing ingredient
        mockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldThrowMethodArgumentTypeMismatchException_whenFindRecipeWithMisMatchIdIsCalled() throws Exception {
        mockMvc.perform(
                        get(ENTITY_API_URL + "/1$")
                                .header("Authorization", JWT)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(4006))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }

    @Test
    void shouldMethodNotAllowed_whenFindRecipeByPostMethodIsCalled() throws Exception {
        mockMvc.perform(
                        post(ENTITY_API_URL + "/1")
                                .header("Authorization", JWT)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

}
