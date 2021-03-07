package com.rezdy.lunch.service;

import com.rezdy.lunch.entity.Recipe;

import java.time.LocalDate;
import java.util.List;

public interface LunchService {

    List<Recipe> findNonExpiredRecipesOnDate(LocalDate date);

    Recipe getRecipeByTitle(String title);

    List<Recipe> findRecipesWithoutSpecifiedIngredients(List<String> ingredientTitles);

}
