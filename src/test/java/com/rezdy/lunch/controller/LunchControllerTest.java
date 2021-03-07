package com.rezdy.lunch.controller;

import com.rezdy.lunch.entity.Ingredient;
import com.rezdy.lunch.entity.Recipe;
import com.rezdy.lunch.service.LunchService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LunchController.class)
public class LunchControllerTest {

    @MockBean
    private LunchService lunchService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindNonExpiredRecipesOnDate_ReturnRecipes() throws Exception {
        List<Recipe> recipesTest = new ArrayList<>();
        recipesTest.add(createRecipe("Salad", "Beetroot",
                "Cucumber", "Lettuce", "Salad Dressing", "Tomato"));
        recipesTest.add(createRecipe("Ham and Cheese Toastie", "Bread", "Butter", "Ham", "Cheese"));
        recipesTest.add(createRecipe("Hotdog", "Hotdog Bun", "Ketchup", "Sausage", "Mustard"));
        LocalDate localDate = LocalDate.of(2023, 1, 1);
        when(lunchService.findNonExpiredRecipesOnDate(localDate)).thenReturn(recipesTest);
        mockMvc.perform(MockMvcRequestBuilders.get("/lunch").param("date", "2023-01-01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(3)))
                .andExpect(jsonPath("$.[*].title",
                        Matchers.hasItems("Salad", "Ham and Cheese Toastie", "Hotdog")
                ))
                .andExpect(jsonPath("$..[*].title",
                        Matchers.hasItems("Beetroot", "Cucumber", "Lettuce", "Salad Dressing", "Tomato",
                                "Bread", "Butter", "Ham", "Cheese", "Hotdog Bun", "Ketchup", "Sausage", "Mustard")));
    }

    @Test
    public void testFindNonExpiredRecipesOnDate_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/lunch").param("date", "2030-01"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetRecipeByTitle_ReturnRecipe() throws Exception {
        String title = "Salad";
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        when(lunchService.getRecipeByTitle(title)).thenReturn(recipe);
        mockMvc.perform((MockMvcRequestBuilders.get("/getRecipe/Salad")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", Matchers.is(title)));
    }

    @Test
    public void testGetRecipeByTitle_NotFound() throws Exception {
        String title = "Dumpling";
        when(lunchService.getRecipeByTitle(title)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/getRecipe/Dumpling"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testFindRecipesWithoutSpecifiedIngredients_ReturnRecipes() throws Exception {
        String[] ingredientTitles = {"Beetroot","Cucumber","Hotdog Bun","Ketchup","Lettuce",
                "Milk","Mustard","Salad Dressing","Spinach","Tomato"};
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(createRecipe("Fry-up"));
        recipes.add(createRecipe("Ham and Cheese Toastie"));
        when(lunchService.findRecipesWithoutSpecifiedIngredients(Arrays.asList(ingredientTitles)))
                .thenReturn(recipes);
        mockMvc.perform(MockMvcRequestBuilders.get("/findRecipes")
                .param("excludedIngredients", ingredientTitles))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$.[*].title", Matchers.hasItems("Fry-up", "Ham and Cheese Toastie")));
    }

    @Test
    public void testFindRecipesWithoutSpecifiedIngredients_NoRecipes() throws Exception {
        String[] ingredientTitles = {"Bacon", "Bread", "Hotdog Bun", "Eggs", "Beetroot"};
        List<Recipe> recipes = new ArrayList<>();
        when(lunchService.findRecipesWithoutSpecifiedIngredients(Arrays.asList(ingredientTitles)))
                .thenReturn(recipes);
        mockMvc.perform(MockMvcRequestBuilders.get("/findRecipes")
                .param("excludedIngredients", ingredientTitles))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(0)));
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
