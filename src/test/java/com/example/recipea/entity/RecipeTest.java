package com.example.recipea.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecipeTest {

    Recipe recipe;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
    }

    @Test
    void testTitle() {
        String title = "Chicken Kabab";
        recipe.setTitle(title );
        assertEquals(title, recipe.getTitle());
    }

    @Test
    void testId() {
        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        assertEquals(recipe1,recipe1);

        Recipe recipe2 = new Recipe();
        recipe2.setId(recipe1.getId());
        assertEquals(recipe1,recipe2);

        recipe2.setId(2L);
        recipe1.setId(null);
        assertNotEquals(recipe1,recipe2);
    }

    @Test
    void testEqual() {
       assertEquals(recipe,recipe);
       assertEquals(recipe.hashCode(),recipe.hashCode());
       assertNotEquals(recipe,new Ingredient());

       Ingredient ingredient=new Ingredient("salt");
       ingredient.setId(100L);
       recipe.setIngredients(Set.of(ingredient));

       assertEquals(1,recipe.getIngredients().size());
       assertThat(recipe.getIngredients(),containsInAnyOrder(ingredient));

    }

}
