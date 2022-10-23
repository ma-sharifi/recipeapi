package com.example.recipea.repository;

import com.example.recipea.RecipeaApplication;
import com.example.recipea.entity.Ingredient;
import com.example.recipea.entity.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mahdi Sharifi
 */

@SpringBootTest(classes = RecipeaApplication.class)
class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    void shouldSaveAndLoad() {
        //The first saves a newly created Ingredient in the database
        Long id = transactionTemplate.execute((ts) -> {
            Ingredient recipeRice = new Ingredient("oil-corn");
            ingredientRepository.save(recipeRice);
            return recipeRice.getId();
        });
        //The second transaction loads the Ingredient and verifies that its fields are properly initialized
        transactionTemplate.execute((ts) -> {
            Ingredient ingredient = ingredientRepository.findById(id).get();
            assertEquals(id, ingredient.getId());
            assertEquals("oil-corn", ingredient.getTitle());
            return null;
        });
    }
}