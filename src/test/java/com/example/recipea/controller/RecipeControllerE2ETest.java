package com.example.recipea.controller;

import com.example.recipea.EndToEndTest;
import com.example.recipea.entity.Recipe;
import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.service.dto.ResponseDto;
import com.example.recipea.service.mapper.RecipeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import javax.annotation.PostConstruct;
import java.util.Collections;

import static com.example.recipea.util.ConvertorUtil.toResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mahdi Sharifi
 */
@EndToEndTest
class RecipeControllerE2ETest {

    private static final String DEFAULT_TITLE = "Sushi E2E";
    private static final String DEFAULT_INSTRUCTION = "Sushi E2E is a Japanese dish of prepared vinegared rice, usually with some sugar and salt, accompanied by a variety of ingredients, such as seafood, often raw, and vegetables.";
    private static final Integer DEFAULT_SERVE = 100;
    private static final String ENTITY_API_URL = "/v1/recipes";
    private static final String JWT = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJyZWNpcGVhIiwic3ViIjoibWFoZGkiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY2Mzg1OTQzLCJleHAiOjE2Njg5ODE1NDN9.v4ghT5iLyItNHJ70062INiUSbHkgYx1LvTcDwWXV1_mTS0RZ1IlAgQB1BaRUm7PB9Cb4B5eO1lvWq--CgNq7rQ";
    Recipe recipe;
    private String uri;
    @LocalServerPort
    private int port;
    @Autowired
    private RecipeController recipeController;
    @Autowired
    private RecipeMapper recipeMapper;
    @Autowired
    private TestRestTemplate restTemplate;

    public static Recipe createEntity() {
        return new Recipe(DEFAULT_TITLE, DEFAULT_INSTRUCTION, DEFAULT_SERVE);
    }

    @PostConstruct
    public void init() {
        uri = "http://localhost:" + port;
    }

    @Test
    void contextLoads() {
        assertThat(recipeController).isNotNull();
    }

    @BeforeEach
    public void initTest() {
        recipe = createEntity();
    }

    @Test
    void shouldCreateRecipeThenReturnRecipe_whenCreateIsCalled_thenGetRecipeIsCalled() {
        // Create Recipe
        RecipeDto dto = recipeMapper.toDto(recipe);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", JWT);
        HttpEntity<RecipeDto> entity = new HttpEntity<>(dto, headers);
        ResponseEntity<ResponseDto> responseEntityCreate = this.restTemplate.exchange(uri + ENTITY_API_URL, HttpMethod.POST, entity, ResponseDto.class);

        //Test Created successfully
        String urlCreatedObject = responseEntityCreate.getHeaders().get("Location").get(0);//get the location of saved recipe-> /v1/recipes/6
        assertEquals(HttpStatus.CREATED, responseEntityCreate.getStatusCode());

        // Get saved Recipe
        HttpEntity<String> entityGet = new HttpEntity<>(headers);
        ResponseEntity<RecipeDto> responseEntity = this.restTemplate.exchange(uri + urlCreatedObject, HttpMethod.GET, entityGet, RecipeDto.class);

        //Test Fetch saved Recipe successfully
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldReturnNoContent_whenCreateIsCalled_thenDeleteIsCalled() {
        // Create Recipe
        recipe.setTitle("Dummy data for test delete");
        RecipeDto dto = recipeMapper.toDto(recipe);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", JWT);
        HttpEntity<RecipeDto> entity = new HttpEntity<>(dto, headers);
        ResponseEntity<ResponseDto> responseEntityCreate = this.restTemplate.exchange(uri + ENTITY_API_URL, HttpMethod.POST, entity, ResponseDto.class);

        //Test Created successfully
        String urlCreatedObject = responseEntityCreate.getHeaders().get("Location").get(0);//get the location of saved recipe-> /v1/recipes/6
        assertEquals(HttpStatus.CREATED, responseEntityCreate.getStatusCode());

        // Get saved Recipe
        HttpEntity<String> entityGet = new HttpEntity<>(headers);
        ResponseEntity<RecipeDto> responseEntity = this.restTemplate.exchange(uri + urlCreatedObject, HttpMethod.DELETE, entityGet, RecipeDto.class);

        //Test Fetch saved Recipe successfully
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void shouldReturnRecipe_whenCreateIsCalled_thenUpdateIsCalled() {
        // Create Recipe
        recipe.setTitle("Dummy data for test update");
        RecipeDto dto = recipeMapper.toDto(recipe);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", JWT);
        HttpEntity<RecipeDto> entity = new HttpEntity<>(dto, headers);
        ResponseEntity<String> responseEntityCreated = this.restTemplate.exchange(uri + ENTITY_API_URL, HttpMethod.POST, entity, String.class);

        //Test Created successfully
        String urlCreatedObject = responseEntityCreated.getHeaders().get("Location").get(0);//get the location of saved recipe-> /v1/recipes/6
        assertEquals(HttpStatus.CREATED, responseEntityCreated.getStatusCode());

        ResponseDto<RecipeDto> responseDtoRecipeDto = toResponseDto(responseEntityCreated.getBody());

        System.out.println("####responseDtoRecipeDto: " + responseDtoRecipeDto);

        RecipeDto recipeDtoCreated = responseDtoRecipeDto.getPayload().get(0);
        assertEquals("Dummy data for test update", recipeDtoCreated.getTitle());

        recipeDtoCreated.setTitle("Updated title Dummy data for test update");
        recipeDtoCreated.setInstruction("Updated instruction");

        // Get saved Recipe
        HttpEntity<RecipeDto> entityPut = new HttpEntity<>(recipeDtoCreated, headers);
        ResponseEntity<String> responseEntityUpdated = this.restTemplate.exchange(uri + urlCreatedObject, HttpMethod.PUT, entityPut, String.class);

        //Test updated recipe successfully
        assertEquals(HttpStatus.OK, responseEntityUpdated.getStatusCode());

        RecipeDto recipeDtoUpdated = toResponseDto(responseEntityUpdated.getBody()).getPayload().get(0);
        assertEquals(recipeDtoCreated.getId(), recipeDtoUpdated.getId());
        assertEquals("Updated title Dummy data for test update", recipeDtoUpdated.getTitle());
        assertEquals("Updated instruction", recipeDtoUpdated.getInstruction());
    }


}
