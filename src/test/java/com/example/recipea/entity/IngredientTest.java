package com.example.recipea.entity;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class IngredientTest {

    Ingredient ingredient;

    @Test
    void testIngredient() {
        Ingredient ingredient1 = new Ingredient("salt");
        ingredient1.setId(1L);
        Recipe recipe=new Recipe("sushi", "instruction", 1,"mahdi");
        recipe.setId(10L);
        ingredient1.setRecipes(Set.of(recipe));

        assertEquals(ingredient1, ingredient1);

        assertEquals(1,ingredient1.getRecipes().size());
        assertThat(ingredient1.getRecipes(), containsInAnyOrder(recipe));

        assertNotEquals(ingredient1, new Recipe());

        assertEquals("salt", ingredient1.getTitle());
        assertEquals(1, ingredient1.getId());

        Ingredient ingredient2 = new Ingredient("oil");
        assertEquals("oil", ingredient2.getTitle());
        ingredient2.setId(ingredient1.getId());
        assertEquals(1, ingredient1.getId());

        assertEquals(ingredient1, ingredient2);//Base on ID
        assertEquals(ingredient1.hashCode(), ingredient2.hashCode());//Base on ID

        ingredient2.setId(2L);
        assertEquals(2, ingredient2.getId());
        assertNotEquals(ingredient1, ingredient2);

        ingredient1.setId(null);
        assertNotEquals(ingredient1, ingredient2);

    }
}
