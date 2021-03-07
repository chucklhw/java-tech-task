package com.rezdy.lunch.service.impl;

import com.rezdy.lunch.entity.Ingredient;
import com.rezdy.lunch.entity.Recipe;
import com.rezdy.lunch.repository.RecipeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LunchServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private LunchServiceImpl lunchServiceImpl;

    @Test
    public void testFindNonExpiredRecipesOnDate_ThreeRecipesByOrder() {
        Recipe salad = createRecipe(2L, "Salad",
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2024, 1, 1));
        Recipe hotdog = createRecipe(1L, "Hotdog",
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2024, 1, 1));
        Recipe HamAndCheeseToastie = createRecipe(4L, "Ham and Cheese Toastie",
                LocalDate.of(2029, 1, 1),
                LocalDate.of(2030, 1, 1));
        Recipe omelette = createRecipe(3L, "Omelette",
                LocalDate.of(2029, 1, 1),
                LocalDate.of(2030, 1, 1));


        List<Recipe> recipesBeforeSort = new ArrayList<>();
        recipesBeforeSort.add(salad);
        recipesBeforeSort.add(hotdog);
        recipesBeforeSort.add(HamAndCheeseToastie);
        recipesBeforeSort.add(omelette);
        LocalDate localDate = LocalDate.of(2022, 01, 01);
        when(recipeRepository.findNonExpiredRecipesOnDate(localDate)).thenReturn(recipesBeforeSort);
        List<Recipe> recipes = lunchServiceImpl.findNonExpiredRecipesOnDate(localDate);
        verify(recipeRepository).findNonExpiredRecipesOnDate(localDate);

        List<Recipe> recipesAfterSort = new ArrayList<>();
        recipesAfterSort.add(omelette);
        recipesAfterSort.add(HamAndCheeseToastie);
        recipesAfterSort.add(hotdog);
        recipesAfterSort.add(salad);
        Assertions.assertThat(recipes).usingRecursiveComparison()
                .ignoringFields("ingredients").isEqualTo(recipesAfterSort);

    }

    @Test
    public void testGetRecipeByTitle() {
        String title = "Salad";
        Recipe recipeTest = new Recipe();
        recipeTest.setTitle(title);
        when(recipeRepository.getByTitle(title)).thenReturn(recipeTest);
        Recipe recipe = lunchServiceImpl.getRecipeByTitle(title);
        verify(recipeRepository).getByTitle(title);
        org.junit.jupiter.api.Assertions.assertEquals(recipe.getTitle(), title);
    }

    @Test
    public void testFindRecipesWithoutSpecifiedIngredients() {
        String[] titles = {"Beetroot","Cucumber","Hotdog Bun","Ketchup","Lettuce",
                "Milk","Mustard","Salad Dressing","Spinach","Tomato"};
        List<String> ingredientTitles = Arrays.asList(titles);
        List<Recipe> recipesExpected = new ArrayList<>();
        recipesExpected.add(createRecipe(1L, "Fry-up", null, null));
        recipesExpected.add(createRecipe(2L, "Ham and Cheese Toastie", null, null));
        when(recipeRepository.findRecipesWithoutSpecifiedIngredients(ingredientTitles))
                .thenReturn(recipesExpected);
        List<Recipe> recipes = lunchServiceImpl.findRecipesWithoutSpecifiedIngredients(ingredientTitles);
        verify(recipeRepository).findRecipesWithoutSpecifiedIngredients(ingredientTitles);
        Assertions.assertThat(recipes).usingRecursiveComparison()
                .ignoringFields("ingredients").isEqualTo(recipesExpected);
    }

    private Recipe createRecipe(Long recipeId, String recipeTitle, LocalDate bestBefore, LocalDate useBy) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setTitle(recipeTitle);
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setBestBefore(bestBefore);
        ingredient.setUseBy(useBy);
        ingredients.add(ingredient);
        recipe.setIngredients(ingredients);
        return recipe;
    }
}
