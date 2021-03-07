package com.rezdy.lunch.repository;

import com.rezdy.lunch.entity.Ingredient;
import com.rezdy.lunch.entity.Recipe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    public void testFindNonExpiredRecipesOnDate_NoRecipe() {
        LocalDate localDate = LocalDate.of(2031, 1, 2);
        List<Recipe> recipes = recipeRepository.findNonExpiredRecipesOnDate(localDate);
        Assertions.assertThat(recipes).isEmpty();
    }

    @Test
    public void testFindNonExpiredRecipesOnDate_ThreeRecipes() {
        List<Recipe> recipesTest = new ArrayList<>();
        recipesTest.add(createRecipe("Ham and Cheese Toastie", "Bread", "Butter", "Ham", "Cheese"));
        recipesTest.add(createRecipe("Salad", "Beetroot",
                "Cucumber", "Lettuce", "Salad Dressing", "Tomato"));
        recipesTest.add(createRecipe("Hotdog", "Ketchup", "Hotdog Bun", "Sausage", "Mustard"));
        LocalDate localDate = LocalDate.of(2023, 1, 1);
        List<Recipe> recipes = recipeRepository.findNonExpiredRecipesOnDate(localDate);
        Assertions.assertThat(recipes).usingRecursiveComparison().ignoringCollectionOrder()
                .ignoringFields("id", "ingredients.id", "ingredients.bestBefore",
                        "ingredients.useBy", "ingredients.recipes")
                .isEqualTo(recipesTest);
    }

    @Test
    public void testFindNonExpiredRecipesOnDate_AllRecipes() {
        LocalDate localDate = LocalDate.of(2016, 1, 1);
        List<Recipe> recipes = recipeRepository.findNonExpiredRecipesOnDate(localDate);
        org.junit.jupiter.api.Assertions.assertEquals(recipes.size(), recipeRepository.count());
    }

    @Test
    public void testGetByTitle_ReturnRecipe() {
        String title = "Salad";
        Recipe recipe = recipeRepository.getByTitle(title);
        org.junit.jupiter.api.Assertions.assertEquals(recipe.getTitle(), title);
    }

    @Test
    public void testGetByTitle_NoRecipe() {
        String title = "Dumpling";
        Recipe recipe = recipeRepository.getByTitle(title);
        org.junit.jupiter.api.Assertions.assertNull(recipe);
    }

    @Test
    public void testFindRecipesWithoutSpecifiedIngredients_ReturnRecipes() {
        String[] ingredientTitles = {"Beetroot","Cucumber","Hotdog Bun","Ketchup","Lettuce",
                "Milk","Mustard","Salad Dressing","Spinach","Tomato"};
        List<Recipe> recipes = recipeRepository.findRecipesWithoutSpecifiedIngredients(Arrays.asList(ingredientTitles));

        List<Recipe> recipesExpected = new ArrayList<>();
        recipesExpected.add(createRecipe("Fry-up"));
        recipesExpected.add(createRecipe("Ham and Cheese Toastie"));
        Assertions.assertThat(recipes).usingRecursiveComparison().ignoringCollectionOrder()
                .ignoringFields("id", "ingredients").isEqualTo(recipesExpected);
    }

    @Test
    public void testFindRecipesWithoutSpecifiedIngredients_NoRecipe() {
        String[] ingredientTitles = {"Bacon", "Bread", "Hotdog Bun", "Eggs", "Beetroot"};
        List<Recipe> recipes = recipeRepository.findRecipesWithoutSpecifiedIngredients(Arrays.asList(ingredientTitles));
        org.junit.jupiter.api.Assertions.assertEquals(recipes.size(), 0);
    }

    private Recipe createRecipe(String recipeTitle, String... ingredientTitles) {
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeTitle);
        List<Ingredient> ingredients = new ArrayList<>();
        Arrays.stream(ingredientTitles).forEach(ingredientTitle->{
            Ingredient ingredient = new Ingredient();
            ingredient.setTitle(ingredientTitle);
            ingredients.add(ingredient);
        });
        recipe.setIngredients(ingredients);
        return recipe;
    }
}
