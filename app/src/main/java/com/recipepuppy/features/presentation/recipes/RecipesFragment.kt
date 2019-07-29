package com.recipepuppy.features.presentation.recipes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.recipepuppy.R
import com.recipepuppy.core.exception.Failure
import com.recipepuppy.core.extension.*
import com.recipepuppy.core.platform.BaseFragment
import com.recipepuppy.features.presentation.model.RecipeView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_recipes.*

/**
 * Created by danieh on 29/07/2019.
 */
class RecipesFragment : BaseFragment() {

    override fun layoutId() = R.layout.fragment_recipes

    private lateinit var viewModel: RecipesViewModel

    private val recipesAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        viewModel = viewModel(viewModelFactory) {
            observe(recipeList, ::onRecipesFetched)
            failure(failure, ::showError)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_recipes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipesAdapter
        }

        viewModel.getRecipes("onions,garlic", "omelet")
    }

    private fun onRecipesFetched(recipes: List<RecipeView>?) {
        progress_recipes.gone()
        if (recipes != null && recipes.isNotEmpty()) {
            val items = recipes.map { recipeView ->
                RecipeItem(recipeView, clickListenerRecipe = { recipeView ->
                    Toast.makeText(context, "Recipe clicked!", Toast.LENGTH_SHORT).show()
                }, clickListenerFav = { _, _ ->
                    Toast.makeText(context, "Recipe fav clicked!", Toast.LENGTH_SHORT).show()
                })
            }
            recipesAdapter.addAll(items)
            recycler_recipes.visible()
        } // TODO: else
    }

    private fun showError(failure: Failure?) {
        progress_recipes.gone()
        when (failure) {
            is Failure.ServerError -> notify("ServerError")
        }
    }
}