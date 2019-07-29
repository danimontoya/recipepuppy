package com.recipepuppy.features.presentation.recipes

import androidx.lifecycle.MutableLiveData
import com.recipepuppy.core.platform.BaseViewModel
import com.recipepuppy.features.domain.model.Recipe
import com.recipepuppy.features.domain.usecase.GetRecipesUseCase
import com.recipepuppy.features.presentation.model.RecipeView
import javax.inject.Inject

/**
 * Created by danieh on 29/07/2019.
 */
class RecipesViewModel @Inject constructor(private val getRecipesUseCase: GetRecipesUseCase) : BaseViewModel() {

    var recipeList: MutableLiveData<List<RecipeView>> = MutableLiveData()

    private var page: Int = 1

    fun getRecipes(ingredients: String, query: String) = getRecipesUseCase(GetRecipesUseCase.Params(ingredients, query, page)) {
        it.fold(::handleFailure, ::handleRecipes)
    }

    private fun handleRecipes(recipes: List<Recipe>) {
        recipeList.value = recipes.map { it.toRecipeView() }
        page++
    }
}
