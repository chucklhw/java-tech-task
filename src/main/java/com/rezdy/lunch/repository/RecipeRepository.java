package com.rezdy.lunch.repository;

import com.rezdy.lunch.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("select distinct recipe from Recipe as recipe " +
            "where recipe.id not in (select distinct recipe2.id from Recipe as recipe2 " +
            "inner join recipe2.ingredients as ingredient2 where ingredient2.useBy < :date)")
    List<Recipe> findNonExpiredRecipesOnDate(@Param("date") LocalDate date);

    Recipe getByTitle(String title);

    @Query("select distinct recipe from Recipe as recipe " +
            "where recipe.id not in (select distinct recipe2.id from Recipe as recipe2 " +
            "inner join recipe2.ingredients as ingredient2 where ingredient2.title in (:ingredientTitles))")
    List<Recipe> findRecipesWithoutSpecifiedIngredients(@Param("ingredientTitles") List<String> ingredientTitles);
}
