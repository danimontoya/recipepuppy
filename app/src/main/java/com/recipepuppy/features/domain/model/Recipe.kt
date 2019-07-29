package com.recipepuppy.features.domain.model

import com.recipepuppy.features.presentation.model.RecipeView

/**
 * Created by danieh on 29/07/2019.
 */
data class Recipe(
    private val href: String,
    private val ingredients: String,
    private val thumbnail: String,
    private val title: String
) {
    fun toRecipeView() = RecipeView(href, ingredients, thumbnail, title)

    companion object {
        fun empty() = Recipe("", "", "", "")
    }
}
