package com.recipepuppy.features.presentation.favoriterecipes

import androidx.lifecycle.MutableLiveData
import com.recipepuppy.core.interactor.UseCase
import com.recipepuppy.core.platform.BaseViewModel
import com.recipepuppy.features.domain.model.Recipe
import com.recipepuppy.features.domain.usecase.DeleteRecipeUseCase
import com.recipepuppy.features.domain.usecase.GetFavoriteRecipesUseCase
import com.recipepuppy.features.domain.usecase.SaveRecipeUseCase
import com.recipepuppy.features.presentation.model.RecipeView
import javax.inject.Inject

/**
 * Created by danieh on 30/07/2019.
 */
class FavoriteRecipesViewModel @Inject constructor(
    private val getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
) : BaseViewModel() {

    var favoriteRecipes: MutableLiveData<List<RecipeView>> = MutableLiveData()

    fun getFavoriteRecipes() = getFavoriteRecipesUseCase(UseCase.None()) {
        it.fold(::handleFailure, ::handleFavoriteRecipes)
    }

    fun saveRecipe(recipeView: RecipeView) = saveRecipeUseCase(SaveRecipeUseCase.Params(recipeView)) {
        it.fold(::handleFailure, ::handleRecipeSaved)
    }

    fun deleteRecipe(href: String) = deleteRecipeUseCase(DeleteRecipeUseCase.Params(href)) {
        it.fold(::handleFailure, ::handleRecipeDeleted)
    }

    private fun handleFavoriteRecipes(recipes: List<Recipe>) {
        val recipesView = recipes.map { it.toRecipeView() }
        recipesView.map { it.isFavorite = true }
        favoriteRecipes.value = recipesView
    }

    private fun handleRecipeSaved(saved: Boolean) {
        // not needed atm
    }

    private fun handleRecipeDeleted(saved: Boolean) {
        // not needed atm
    }
}
