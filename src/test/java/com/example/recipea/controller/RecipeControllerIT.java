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
import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.service.dto.ResponseDto;
import com.example.recipea.service.mapper.RecipeMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

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
@DisplayName("Recipe controller Integration test")
class RecipeControllerIT {

    private static final String USERNAME_MAHDI = "mahdi";
    private static final String USERNAME_ALEX = "alex";
    private static final String DEFAULT_TITLE = "Sushi";

    private static final String DEFAULT_INSTRUCTION = "Sushi is a Japanese dish of prepared vinegared rice, usually with some sugar and salt, accompanied by a variety of ingredients, such as seafood, often raw, and vegetables.";
    private static final String UPDATED_INSTRUCTION = "Updated Sushi is a Japanese dish of prepared vinegared rice, usually with some sugar and salt, accompanied by a variety of ingredients, such as seafood, often raw, and vegetables. ";

    private static final Integer DEFAULT_SERVE = 1;
    private static final Integer UPDATED_SERVE = 2;

    private static final String ENTITY_API_URL = "/v1/recipes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final String JWT_MAHDI = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2Mzg1OTQzLCJleHAiOjE2Njg5ODE1NDN9.v4ghT5iLyItNHJ70062INiUSbHkgYx1LvTcDwWXV1_mTS0RZ1IlAgQB1BaRUm7PB9Cb4B5eO1lvWq--CgNq7rQ";
    private static final String JWT_ALEX = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoiYWxleCIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NjY1NjAwOTAsImV4cCI6MTY2OTE1NTY5MH0.5rRG9qp1qQL73l-g5eK0W1TIB3sba1W-sd_PCJg7lF5FxS5Q7tdFTj13NqyDcGkgLmtjXN48ruDo7Perq6yhxQ";
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeMapper recipeMapper;
    @Autowired
    private MockMvc mockMvc;
    private Recipe recipe;
    int randomInt= 1;

    public static Recipe createEntity() {
        return new Recipe(DEFAULT_TITLE, DEFAULT_INSTRUCTION, DEFAULT_SERVE, USERNAME_MAHDI);
    }


    @BeforeEach
    public void initTest() {
        randomInt= ThreadLocalRandom.current().nextInt(10000);
        recipe = createEntity();
    }

    /**
     * In Integration test you must test every dependency. Becasue of these dependencies it takes more than other test types.
     */

    @Test
    void shouldReturnAllVegetarian_whenIsVegIsTrue() throws Exception {
        // run query, we expect get the same result from endpoint.
        List<RecipeDto> listSearched = recipeService
                .findByIngredientsAndInstructionAndServeAndVegetarian( null, null, true, null, USERNAME_MAHDI);

        // Get all the recipeList
        MockHttpServletResponse responseMockMvc= mockMvc
                .perform(get(ENTITY_API_URL + "/search?isveg=true").header("Authorization", JWT_MAHDI))
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
    void shouldReturnAllVegetarianBelongToAlex_whenIsVegIsTrue() throws Exception {
        // Initialize the database
        saveRecipeDataForAlex();
        // run query, we expect get the same result from endpoint.
        List<RecipeDto> listSearched = recipeService
                .findByIngredientsAndInstructionAndServeAndVegetarian( null, null, true, null,USERNAME_ALEX);
        // Get all the recipeList
        MockHttpServletResponse responseMockMvc= mockMvc
                .perform(get(ENTITY_API_URL + "/search?isveg=true")
                        .header("Authorization", JWT_ALEX))
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
    void shouldReturnNonVegetarian_whenIsVegIsFalse() throws Exception {
        // run query, we expect get the same result from endpoint.
        List<RecipeDto> listSearched = recipeService
                .findByIngredientsAndInstructionAndServeAndVegetarian(null , null, false, null, USERNAME_MAHDI);

        // Get all the recipeList
        MockHttpServletResponse responseMockMvc = mockMvc
                .perform(get(ENTITY_API_URL + "/search?isveg=false").header("Authorization", JWT_MAHDI))
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
    void shouldReturnRecipesForServe4PersonsAndPotatoesIngredient_whenServe4AndIngredientPotatoesIsCalled() throws Exception {
        // Initialize the database
        // run query, we expect get the same result from endpoint.
        List<RecipeDto> listSearched = recipeService.findByIngredientsAndInstructionAndServeAndVegetarian("potatoes",  null, null, 4, USERNAME_MAHDI);
        // Get all the recipeList
        MockHttpServletResponse responseMockMvc = mockMvc
                .perform(get(ENTITY_API_URL + "/search?serve=4&ingredient=potatoes").header("Authorization", JWT_MAHDI))
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
    @Test
    void shouldReturnRecipesForServe4PersonsAndPotatoesIngredientBelongToAlex_whenServe4AndIngredientPotatoesIsCalled() throws Exception {
        // Initialize the database
        saveRecipeDataForAlex();
        // run query, we expect get the same result from endpoint.
        List<RecipeDto> listSearched = recipeService.findByIngredientsAndInstructionAndServeAndVegetarian("potatoes",  null, null, 4, USERNAME_ALEX);
        // Get all the recipeList
        MockHttpServletResponse responseMockMvc = mockMvc
                .perform(get(ENTITY_API_URL + "/search?serve=4&ingredient=potatoes").header("Authorization", JWT_ALEX))
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
    void shouldReturnRecipesWithoutSalmonIngredientWithOvenInstruction_whenIngredientMinusSalmonAndInstructionStoveIsCalled() throws Exception {

        // Initialize the database
        // run query, we expect get the same result from endpoint.
        List<RecipeDto> listSearched = recipeService
                .findByIngredientsAndInstructionAndServeAndVegetarian("-salmon", "oven", null, null, USERNAME_MAHDI);

        // search all the recipeList from endpoint
        MockHttpServletResponse responseMockMvc = mockMvc
                .perform(get(ENTITY_API_URL + "/search?ingredient=-salmon&instruction=oven").header("Authorization", JWT_MAHDI))
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
                                .header("Authorization", JWT_MAHDI)
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
                                .header("Authorization", JWT_MAHDI)
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
                                .header("Authorization", JWT_MAHDI)
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
                                .header("Authorization", JWT_MAHDI)
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
                                .header("Authorization", JWT_MAHDI)
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
                        .header("Authorization", JWT_MAHDI)
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
                        .header("Authorization", JWT_MAHDI)
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
                .perform(get(ENTITY_API_URL).header("Authorization", JWT_MAHDI))
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
                        .header("Authorization", JWT_MAHDI)
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
                        .header("Authorization", JWT_MAHDI)
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
                        .header("Authorization", JWT_MAHDI)
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
                        .header("Authorization", JWT_MAHDI)
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
                        .header("Authorization", JWT_MAHDI))
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
                                .header("Authorization", JWT_MAHDI)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(4006))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }

    @Test
    void shouldMethodNotAllowed_whenFindRecipeByPostMethodIsCalled() throws Exception {
        mockMvc.perform(
                        post(ENTITY_API_URL + "/1")
                                .header("Authorization", JWT_MAHDI)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    void saveRecipeDataForAlex() {
        List<Ingredient> ingredientList=ingredientRepository.findAll();//read all saved ingredient

        Recipe recipeSalmonKabab = new Recipe("Salmon Kabab-"+ThreadLocalRandom.current().nextInt(100), "This kabab cooked by stove", 4,"alex");
        for (Ingredient ingredient:ingredientList) {
            recipeSalmonKabab.addIngredient(ingredient);
        }
        recipeRepository.saveAndFlush(recipeSalmonKabab);
        //----------------
        Recipe recipeFrenchFries = new Recipe("French fries-"+ThreadLocalRandom.current().nextInt(100), "Cook potato on oven", 1,"alex");
        recipeFrenchFries.setVegetarian(true);
        for (int i = 0; i <= ingredientList.size()/2; i++) {
            recipeFrenchFries.addIngredient(ingredientList.get(i));
        }
        recipeRepository.saveAndFlush(recipeFrenchFries);
    }

}
