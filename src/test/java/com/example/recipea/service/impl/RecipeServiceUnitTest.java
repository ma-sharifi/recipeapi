package com.example.recipea.service.impl;

import com.example.recipea.entity.Recipe;
import com.example.recipea.exception.RecipeNotFoundException;
import com.example.recipea.repository.RecipeRepository;
import com.example.recipea.service.RecipeService;
import com.example.recipea.service.dto.RecipeDto;
import com.example.recipea.service.mapper.RecipeMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Mahdi Sharifi
 *
 * Solitary unit test—Tests a class in isolation using mock objects for the class’s dependencies
 */
@ExtendWith(MockitoExtension.class)
class RecipeServiceUnitTest {//OR RecipeServiceSolitaryTest

    public static final String USERNAME_MAHDI = "mahdi";

    RecipeService service;

    @Mock
    RecipeRepository repository;

    @Captor
    ArgumentCaptor<Recipe> captor;//You should call it during the verification phase of the test.

    @Spy
    RecipeMapper mapper = Mappers.getMapper(RecipeMapper.class);

    @Spy
    EntityManager entityManager;

    @BeforeEach
    void initializeService() {
        service = new RecipeServiceImpl(repository, mapper, entityManager);
    }

    @Test
    void shouldReturnRecipeNotFoundException_whenSaveGetIsCalled() {
        RecipeNotFoundException thrown = Assertions.assertThrows(RecipeNotFoundException.class, () -> {
            service.findOne(1000L, USERNAME_MAHDI);
        });
        assertTrue(thrown.getMessage().contains("Could not find the recipe"));
    }

    @Test
    void shouldReturnRecipeStubbed_whenFindIsCalled() throws Exception {
        Recipe entity = new Recipe("Smoky Rice", "You need to cook the rice with fire and coal", 1,USERNAME_MAHDI);
        entity.setId(100L);

        // Arrange stub save method. It must return given entity.
        when(repository.findByIdAndUsername(100L,USERNAME_MAHDI)).thenReturn(Optional.of(entity));

        RecipeDto expectedRecipeDto = mapper.toDto(entity);

        // Act
        RecipeDto actualRecipeDto = service.findOne(100L, USERNAME_MAHDI);
        verify(repository, times(1)).findByIdAndUsername(100L,USERNAME_MAHDI);
        // Assert
        assertThat(actualRecipeDto, is(equalTo(expectedRecipeDto)));
    }

    @Test
    void shouldReturnRecipeStubbed_whenSaveIsCalled() throws Exception {
        Recipe entity = new Recipe("Smoky Rice", "You need to cook the rice with fire and coal", 1,USERNAME_MAHDI);
        // Arrange stub save method. It must return given entity.
        when(repository.save(any(Recipe.class))).thenReturn(entity);

        RecipeDto actualRecipeDto = mapper.toDto(entity);

        // Act
        RecipeDto expectedRecipeDto = service.save(actualRecipeDto);
        //to verify if the save() method of the mocked repository has been called.
        verify(repository, times(1)).save(any(Recipe.class)); //TODO
        // Assert
        assertThat(actualRecipeDto, is(equalTo(expectedRecipeDto)));
    }

    @Test
    void shouldCaptureSave_whenSaveIsCalled() {

        Recipe entity = new Recipe();
        entity.setTitle("Smoky Rice");
        entity.setServe(4);
        service.save(mapper.toDto(entity));

        //To capture the method arguments, you need to use the capture() method of ArgumentCaptor.
        //You should call it during the verification phase of the test.
        Mockito.verify(repository).save(captor.capture());
        //Note: If the verified methods are called multiple times, then the getValue() method will return the latest captured value.
        assertEquals("Smoky Rice", captor.getValue().getTitle());
        assertEquals(4, captor.getValue().getServe());
    }

    @Test
    void shouldCaptureSaveMultipleTimes_whenSaveIsCalled() {
        Recipe entity1 = new Recipe("Smoky Rice1", "You need to cook the rice with fire and coal1", 1,USERNAME_MAHDI);
        Recipe entity2 = new Recipe("Smoky Rice2", "You need to cook the rice with fire and coal2", 2,USERNAME_MAHDI);
        Recipe entity3 = new Recipe("Smoky Rice3", "You need to cook the rice with fire and coal3", 3,USERNAME_MAHDI);

        service.save(mapper.toDto(entity1));
        service.save(mapper.toDto(entity2));
        service.save(mapper.toDto(entity3));

        Mockito.verify(repository, Mockito.times(3)).save(captor.capture());

        List<Recipe> recipeListExpected = captor.getAllValues();

        assertEquals("Smoky Rice1", recipeListExpected.get(0).getTitle());
        assertEquals(1, recipeListExpected.get(0).getServe());
        assertEquals("Smoky Rice2", recipeListExpected.get(1).getTitle());
        assertEquals(2, recipeListExpected.get(1).getServe());
        assertEquals("Smoky Rice3", recipeListExpected.get(2).getTitle());
        assertEquals(3, recipeListExpected.get(2).getServe());
    }

}