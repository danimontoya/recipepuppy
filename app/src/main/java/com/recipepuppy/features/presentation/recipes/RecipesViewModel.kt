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
    var restartSearch: MutableLiveData<Boolean> = MutableLiveData()

    private var currentPage: Int = 1
    private var currentIngredients: String = "onions,garlic"

    fun getRecipes(ingredients: String) {
        if (currentIngredients != ingredients) {
            currentIngredients = ingredients
            currentPage = 1
            restartSearch.value = true
        }
        getRecipesUseCase(GetRecipesUseCase.Params(currentIngredients, currentPage)) {
            it.fold(::handleFailure, ::handleRecipes)
        }
    }

    private fun handleRecipes(recipes: List<Recipe>) {
        recipeList.value = recipes.map { it.toRecipeView() }
        currentPage++
    }
}
