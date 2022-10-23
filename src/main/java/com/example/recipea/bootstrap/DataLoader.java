package com.example.recipea.bootstrap;

import com.example.recipea.entity.Ingredient;
import com.example.recipea.entity.Recipe;
import com.example.recipea.repository.IngredientRepository;
import com.example.recipea.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Set;

/**
 * @author Mahdi Sharifi
 * We fill table with some mock data here
 */

@Configuration
@Profile("!prod")
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final Environment environment;

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public DataLoader(IngredientRepository ingredientRepository, RecipeRepository recipeRepository, Environment environment) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        log.info("#Currently active profile - " + Arrays.toString(environment.getActiveProfiles()));
        log.info("#mock data is generating.....");
        loadData();
        log.info("#mock data is generated.");
    }

    public void loadData() {
        Ingredient ingredientOil=new Ingredient("oil");
        ingredientRepository.save(ingredientOil);
        Ingredient ingredientSalmon=new Ingredient("salmon");
        ingredientRepository.save(ingredientSalmon);

        Ingredient ingredientPotato=new Ingredient("potatoes");
        ingredientRepository.save(ingredientPotato);

        Recipe recipeSalmonKabab = new Recipe("Salmon Kabab", "This kabab cooked by stove", 1,"mahdi");
        recipeSalmonKabab.addIngredient(ingredientOil);
        recipeSalmonKabab.addIngredient(ingredientSalmon);

        recipeRepository.save(recipeSalmonKabab);
        log.debug("#recipeSalmonKabab: " + recipeSalmonKabab);

        //----------------
        Recipe recipeFrenchFries = new Recipe("French fries", "Cook potato on oven", 4,"mahdi");
        recipeFrenchFries.setVegetarian(true);
        recipeFrenchFries.addIngredient(ingredientPotato);
        recipeFrenchFries.addIngredient(ingredientOil);

        recipeRepository.save(recipeFrenchFries);
        log.debug("#Recipe_Rice: " + recipeFrenchFries);
    }
}
