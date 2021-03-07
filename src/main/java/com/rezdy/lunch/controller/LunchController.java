package com.rezdy.lunch.controller;

import com.rezdy.lunch.entity.Recipe;
import com.rezdy.lunch.exception.ResourceNotFoundException;
import com.rezdy.lunch.service.LunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class LunchController {

    private LunchService lunchService;

    @Autowired
    public LunchController(LunchService lunchService) {
        this.lunchService = lunchService;
    }

    @GetMapping("/lunch")
    public List<Recipe> findNonExpiredRecipesOnDate(@RequestParam("date") String date) {
        return lunchService.findNonExpiredRecipesOnDate(LocalDate.parse(date));
    }

    @GetMapping("/getRecipe/{title}")
    public Recipe getRecipeByTitle(@PathVariable String title) {
        Recipe recipe = lunchService.getRecipeByTitle(title);
        if(recipe == null) {
            throw new ResourceNotFoundException();
        }
        return recipe;
    }

    @GetMapping("/findRecipes")
    public List<Recipe> findRecipesWithoutSpecifiedIngredients(
            @RequestParam("excludedIngredients") List<String> ingredientTitles) {
        List<Recipe> recipes = lunchService.findRecipesWithoutSpecifiedIngredients(ingredientTitles);
        return recipes;
    }

}
