package com.recipepuppy.features.domain.model

/**
 * Created by danieh on 31/07/2019.
 */
data class RecipesResult(
    val recipes: List<Recipe>,
    val favRecipes: List<Recipe>
)