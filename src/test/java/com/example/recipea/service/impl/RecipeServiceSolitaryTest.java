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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
 */
@ExtendWith(MockitoExtension.class)
class RecipeServiceSolitaryTest {

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
            service.findOne(1000L);
        });
        assertTrue(thrown.getMessage().contains("Could not find the recipe"));
    }

    @Test
    void shouldReturnRecipeStubbed_whenFindIsCalled() throws Exception {
        Recipe entity = new Recipe("Smoky Rice", "You need to cook the rice with fire and coal", 1);
        entity.setId(100L);

        // Arrange stub save method. It must return given entity.
        when(repository.findById(100L)).thenReturn(Optional.of(entity));

        RecipeDto expectedRecipeDto = mapper.toDto(entity);

        // Act
        RecipeDto actualRecipeDto = service.findOne(100L);
        verify(repository, times(1)).findById(100L);
        // Assert
        assertThat(actualRecipeDto, is(equalTo(expectedRecipeDto)));
    }

    @Test
    void shouldReturnRecipeStubbed_whenSaveIsCalled() throws Exception {
        Recipe entity = new Recipe("Smoky Rice","You need to cook the rice with fire and coal",1);

        // Arrange stub save method. It must return given entity.
        when(repository.save(any(Recipe.class))).thenReturn(entity);

        RecipeDto actualRecipeDto = mapper.toDto(entity);

        // Act
        RecipeDto expectedRecipeDto = service.save(actualRecipeDto);
        //to verify if the save() method of the mocked repository has been called.
        verify(repository,times(1)).save(any(Recipe.class)); //TODO
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
        assertEquals(4,captor.getValue().getServe());
    }

    @Test
    void shouldCaptureSaveMultipleTimes_whenSaveIsCalled() {
        Recipe entity1 = new Recipe("Smoky Rice1", "You need to cook the rice with fire and coal1", 1);
        Recipe entity2 = new Recipe("Smoky Rice2", "You need to cook the rice with fire and coal2", 2);
        Recipe entity3 = new Recipe("Smoky Rice3", "You need to cook the rice with fire and coal3", 3);

        service.save(mapper.toDto(entity1));
        service.save(mapper.toDto(entity2));
        service.save(mapper.toDto(entity3));

        Mockito.verify(repository,Mockito.times(3)).save(captor.capture());

        List<Recipe> recipeListExpected = captor.getAllValues();

        assertEquals("Smoky Rice1", recipeListExpected.get(0).getTitle());
        assertEquals(1, recipeListExpected.get(0).getServe());
        assertEquals("Smoky Rice2", recipeListExpected.get(1).getTitle());
        assertEquals(2, recipeListExpected.get(1).getServe());
        assertEquals("Smoky Rice3", recipeListExpected.get(2).getTitle());
        assertEquals(3, recipeListExpected.get(2).getServe());
    }



//    @Test
//    void shouldReturnListOfRecipe_whenFindAllIsCalled() throws Exception {
//        Recipe entity1 = new Recipe("Smoky Rice1", "You need to cook the rice with fire and coal1", 1);
//        Recipe entity2 = new Recipe("Smoky Rice2", "You need to cook the rice with fire and coal2", 2);
//        Recipe entity3 = new Recipe("Smoky Rice3", "You need to cook the rice with fire and coal3", 3);
//        entity1.setId(100L);
//        entity2.setId(200L);
//        entity3.setId(300L);
//
//        List<RecipeDto> actualRecipeDtoList = List.of(mapper.toDto(entity1), mapper.toDto(entity2), mapper.toDto(entity3));
////        Page<RecipeDto> findAll(Pageable pageable)
//        Pageable pageable = PageRequest.of(0, 10);
//        // Arrange stub save method. It must return given entity.
////        Page<RecipeDto> findAll(Pageable pageable);
//        Page<Recipe> recipePage = Mockito.mock(Page.class);
//        List<Recipe> recipeListMock = Mockito.mock(List.class);
//        when(repository.findAll(any(Pageable.class))).thenReturn(recipePage);
//        // Act
//        List<RecipeDto> recipeDtoList = service.findAll(pageable).getContent();
//        //--------
////
//        verify(repository, times(1)).findAll(pageable);
//
//        // Assert
//        assertThat(actualRecipeDtoList, is(equalTo(recipeDtoList)));
//    }

//    @Test//https://github.com/pkainulainen/spring-data-jpa-examples/blob/master/tutorial-part-eight/src/test/java/net/petrikainulainen/spring/datajpa/repository/PaginatingPersonRepositoryImplTest.java
//    void findPersonsForPage() {//
//        List<RecipeDto> expected = new ArrayList<>();
//        Page foundPage = new PageImpl<RecipeDto>(expected);
//
//        Pageable pageable =PageRequest.of(0, 10);
//        when(repository.findAll(any(Pageable.class))).thenReturn(foundPage);
//
//        List<RecipeDto> actual = service.findAll(pageable).getContent();
//
//        assertEquals(expected, actual);
//
//        ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
//        verify(repository, times(1)).findAll(pageSpecificationArgument.capture());
//        verifyNoMoreInteractions(repository);
////
//        Pageable pageSpecification = pageSpecificationArgument.getValue();
////
//        assertEquals(1, pageSpecification.getPageNumber());
//        assertEquals(PaginatingPersonRepositoryImpl.NUMBER_OF_PERSONS_PER_PAGE, pageSpecification.getPageSize());
//        assertEquals(Sort.Direction.ASC, pageSpecification.getSort().getOrderFor(PROPERTY_LASTNAME).getDirection());
//
//    }


//    @Test
//    void findByIngredientsAndInstructionAndServeAndVegetarian() {
//    }
//
//    @Test
//    void findAll() {
//    }
//
//    @Test
//    void findOne() {
//    }
//
//    @Test
//    void save() {
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void delete() {
//    }

}