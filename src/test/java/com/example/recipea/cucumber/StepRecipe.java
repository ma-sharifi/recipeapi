//package com.example.recipea.cucumber;
//
//import com.example.recipea.entity.Ingredient;
//import com.example.recipea.entity.Recipe;
//import io.cucumber.java.DataTableType;
//import io.cucumber.java.en.And;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @author Mahdi Sharifi
// * @since 10/19/22
// */
//public class StepRecipe {
//
//    Recipe recipe=new Recipe();
//
//    @Given("recipe is {string}")
//    public void recipe_is(String string) {
//        // Write code here that turns the phrase above into concrete actions
//    }
//    @When("I ask whether it's Vegetarian")
//    public void i_ask_whether_it_s_vegetarian() {
//        // Write code here that turns the phrase above into concrete actions
//    }
//    @Then("I should be said {string}")
//    public void i_should_be_said(String string) {
//        // Write code here that turns the phrase above into concrete actions
//    }
//
//    @DataTableType(replaceWithEmptyString = "[anonymous]")
//    public Recipe toRecipe(Map<String, String> row) {
//
//        recipe.setId(Long.parseLong(row.get("id")));
//        recipe.setTitle( row.get("title"));
//        recipe.setInstruction( row.get("instruction"));
//        recipe.setServe(  Integer.parseInt(row.get("serve")));
//
//        return recipe;
//    }
//    @DataTableType(replaceWithEmptyString = "[anonymous]")
//    public Ingredient toIngredient(Map<String, String> row) {
//
//        Ingredient ingredient = new Ingredient();
//        ingredient.setId(Long.parseLong(row.get("id")));
//        ingredient.setTitle(row.get("title"));
//
//        recipe.addIngredient(ingredient);
//        return ingredient;
//    }
//
//    @Given("user wants to create an recipe with the following attributes")
//    public void user_wants_to_create_an_recipe_with_the_following_attributes(List<Recipe> recipeList) {
////        List<Pojo> recipeList = dataTable.asList(Pojo.class);
//
//        for (Recipe recipe : recipeList) {
//            System.out.println("#Recipe: "+recipe);
//
//        }
//        // Write code here that turns the phrase above into concrete actions
//        // For automatic transformation, change DataTable to one of
//        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
//        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
//        // Double, Byte, Short, Long, BigInteger or BigDecimal.
//        //
//        // For other transformations you can register a DataTableType.
//
//        Recipe recipeChickenKabab = new Recipe();
//        recipeChickenKabab.setTitle("Chicken Kabab");
//        recipeChickenKabab.setServe(2);
//        recipeChickenKabab.setInstruction("This kabab cooked by oven");
//        recipeChickenKabab.addIngredient(new Ingredient("oil"));
//        recipeChickenKabab.addIngredient(new Ingredient("chicken"));
//        recipeChickenKabab.addIngredient(new Ingredient("potatoes"));
//
////        recipeChickenKabab.addTag(new Tag("Vegetarian"));
////        recipeChickenKabab.addTag(new Tag("Gluten Free"));//Gluten Free, Vegetarian,-Diet
//
////        recipeRepository.save(recipeChickenKabab);
////        log.debug("#Recipe_Chicken: " + recipeChickenKabab);
////
////        //----------------
////        Recipe recipeRice = new Recipe("Cook rice", "Cook rice on fire", 4);
////        recipeRice.addIngredient(new Ingredient("rice"));
////        recipeRice.addIngredient(new Ingredient("salt"));
////        recipeRice.addIngredient(new Ingredient("potatoes"));
////        recipeRice.addIngredient(new Ingredient("salmon"));
////        recipeRice.addTag(new Tag("Gluten Free"));
////        recipeRice.addTag(new Tag("Diet"));
////
////        recipeRepository.save(recipeRice);
////        log.debug("#Recipe_Rice: " + recipeRice);
////
////        throw new io.cucumber.java.PendingException();
//    }
//    @And("with the following ingredients")
//    public void withTheFollowingIngredients(List<Ingredient> ingredientList) {
//        for (Ingredient ingredient : ingredientList) {
//            System.out.println("#Ingredient0: "+ingredient);
//
//        }
////        throw new io.cucumber.java.PendingException();
//    }
////    @And("with the following Tag")
////    public void withTheFollowingTag(List<Tag> tagList) {
////        for (Tag tag : tagList) {
////            System.out.println("#tag: "+tag);
////        }
////    }
//
//    @When("user saves the new recipe {string}")//WITH ALL REQUIRED FIELDS
//    public void user_saves_the_new_recipe(String string) {
//        System.out.println("#string: "+string+" ;Recipe:" + recipe );
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }
//    @Then("the save {string}")
//    public void the_save(String string) {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }
//
//
//
//
////
////    @Then("the save {string}")
////    public void theSaveISSUCCESSFUL() {
////    }
//}
