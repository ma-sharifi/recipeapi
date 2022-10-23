package com.example.recipea.repository;

import com.example.recipea.RecipeaApplication;
import com.example.recipea.entity.Ingredient;
import com.example.recipea.entity.Recipe;
import com.example.recipea.service.dto.RecipeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author Mahdi Sharifi
 */
@SpringBootTest(classes = RecipeaApplication.class)
class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    void shouldSaveAndLoadAndDelete() {
        //The first saves a newly created Ingredient in the database
        String titleIngredient="oil-sunflower-"+ ThreadLocalRandom.current().nextInt(1000);//because we have constraint on title
        Ingredient recipeRiceSaved = transactionTemplate.execute((ts) -> {
            Ingredient recipeRice = new Ingredient(titleIngredient);
            ingredientRepository.save(recipeRice);
            return recipeRice;
        });
        //The second transaction saves a newly created Recipe in the database
        String titleRecipe="French fries-"+ ThreadLocalRandom.current().nextInt(1000);//because we have constraint on title
        Long id = transactionTemplate.execute((ts) -> {
            Recipe recipeRice = new Recipe(titleRecipe, "Cook potato on oven", 2,"mahdi");
            recipeRice.setVegetarian(true);
            recipeRice.addIngredient(recipeRiceSaved);
            recipeRepository.save(recipeRice);
            return recipeRice.getId();
        });
        //The third transaction loads the Recipe and verifies that its fields are properly initialized
        Recipe recipe= transactionTemplate.execute((ts) -> {
            Recipe recipePersisted = recipeRepository.findById(id).get();
            assertEquals(id, recipePersisted.getId());
            assertEquals(titleRecipe, recipePersisted.getTitle());
            assertEquals("Cook potato on oven", recipePersisted.getInstruction());
            assertEquals(2, recipePersisted.getServe());
            assertTrue(recipePersisted.isVegetarian());
            assertThat(recipePersisted.getIngredients()).hasSize(1);

            return recipePersisted;
        });

        //The forth transaction delete recipe and verifies it is deleted
        transactionTemplate.execute((ts) -> {
            recipeRepository.delete(recipe);
            return null;
        });

        //The fifth transaction loads the Recipe and verifies that its fields are properly initialized
          transactionTemplate.execute((ts) -> {
            Optional<Recipe> recipeOptionalPersisted = recipeRepository.findById(id);
            assertFalse(recipeOptionalPersisted.isPresent());
            return null;
        });

    }

    @Test
    void shouldUpdateSavedRecipe(){
        //At first transaction saves a newly created Recipe in the database
        String title="French fries-"+ ThreadLocalRandom.current().nextInt(1000);
        Recipe recipeSave = transactionTemplate.execute((ts) -> {
            Recipe recipeRice = new Recipe(title, "Cook potato on oven", 2,"mahdi");
            recipeRice.setVegetarian(true);
            recipeRepository.save(recipeRice);
            assertEquals(title, recipeRice.getTitle());
            assertEquals("Cook potato on oven", recipeRice.getInstruction());
            assertEquals(2, recipeRice.getServe());
            assertTrue(recipeRice.isVegetarian());
            return recipeRice;
        });
        //The second transaction update the Recipe and verifies that its fields are properly initialized
         String titleForUpdate="Updated French fries-"+ThreadLocalRandom.current().nextInt(1000);
         transactionTemplate.execute((ts) -> {
             recipeSave.setTitle(titleForUpdate);
             recipeSave.setInstruction("Updated Cook potato on oven");
             recipeSave.setServe(3);
             recipeSave.setVegetarian(false);
            recipeRepository.save(recipeSave);
            return null;
        });
        //The third transaction loads the Recipe and verifies that its fields are properly updated
         transactionTemplate.execute((ts) -> {
            Recipe recipePersisted = recipeRepository.findById(recipeSave.getId()).get();

            assertEquals(recipeSave.getId(), recipePersisted.getId());
            assertEquals(titleForUpdate, recipePersisted.getTitle());
            assertEquals("Updated Cook potato on oven", recipePersisted.getInstruction());
            assertEquals(3, recipePersisted.getServe());
            assertFalse(recipePersisted.isVegetarian());
            return null;
        });
    }


}
