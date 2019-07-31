package com.recipepuppy.features.domain.usecase

import com.recipepuppy.core.interactor.UseCase
import com.recipepuppy.features.data.repository.RecipesRepository
import com.recipepuppy.features.domain.model.Recipe
import com.recipepuppy.features.presentation.model.RecipeView
import javax.inject.Inject

/**
 * Created by danieh on 30/07/2019.
 */
class SaveRecipeUseCase @Inject constructor(private val recipesRepository: RecipesRepository) :
        UseCase<Boolean, SaveRecipeUseCase.Params>() {

    override suspend fun run(params: Params) =
            recipesRepository.saveRecipe(Recipe(params.recipe.href, params.recipe.ingredients, params.recipe.thumbnail, params.recipe.title))

    data class Params(val recipe: RecipeView)
}