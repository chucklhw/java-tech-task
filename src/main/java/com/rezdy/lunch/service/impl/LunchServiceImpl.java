package com.rezdy.lunch.service.impl;

import com.rezdy.lunch.entity.Ingredient;
import com.rezdy.lunch.entity.Recipe;
import com.rezdy.lunch.repository.RecipeRepository;
import com.rezdy.lunch.service.LunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class LunchServiceImpl implements LunchService {

    private RecipeRepository recipeRepository;

    @Autowired
    public LunchServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public List<Recipe> findNonExpiredRecipesOnDate(LocalDate date) {
        List<Recipe> recipes = recipeRepository.findNonExpiredRecipesOnDate(date);
        Comparator<Recipe> recipesComparator = (r1, r2) -> {
            Optional<Ingredient> ingredientOptional1 = r1.getIngredients().stream()
                    .filter(ingredient -> ingredient.getBestBefore().isBefore(date)
                            || ingredient.getBestBefore().isEqual(date))
                    .findAny();
            Optional<Ingredient> ingredientOptional2 = r2.getIngredients().stream()
                    .filter(ingredient -> ingredient.getBestBefore().isBefore(date)
                            || ingredient.getBestBefore().isEqual(date))
                    .findAny();
            if(ingredientOptional1.isPresent() && ingredientOptional2.isEmpty()) {
                return 1;
            } else if (ingredientOptional1.isEmpty() && ingredientOptional2.isPresent()) {
                return -1;
            }
            return r1.getId().compareTo(r2.getId());
        };
        recipes.sort(recipesComparator);
        return recipes;
    }

    @Transactional
    public Recipe getRecipeByTitle(String title) {
        Recipe recipe = recipeRepository.getByTitle(title);
        return recipe;
    }

    @Transactional
    public List<Recipe> findRecipesWithoutSpecifiedIngredients(List<String> ingredientTitles) {
        List<Recipe> recipes = recipeRepository.findRecipesWithoutSpecifiedIngredients(ingredientTitles);
        return recipes;
    }
}
