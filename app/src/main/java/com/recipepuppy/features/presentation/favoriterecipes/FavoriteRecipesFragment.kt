package com.recipepuppy.features.presentation.favoriterecipes

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.recipepuppy.R
import com.recipepuppy.core.exception.Failure
import com.recipepuppy.core.extension.*
import com.recipepuppy.core.platform.BaseFragment
import com.recipepuppy.features.data.room.entity.RecipeInfo
import com.recipepuppy.features.presentation.model.RecipeView
import com.recipepuppy.features.presentation.recipes.RecipeItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_favorite_recipes.*
import timber.log.Timber

/**
 * Created by danieh on 30/07/2019.
 */
class FavoriteRecipesFragment : BaseFragment() {

    companion object {
        private val TAG = FavoriteRecipesFragment::class.java.simpleName
    }

    override fun layoutId() = R.layout.fragment_favorite_recipes

    private lateinit var viewModel: FavoriteRecipesViewModel

    private val recipesAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setHasOptionsMenu(true)

        viewModel = viewModel(viewModelFactory) {
            observe(favoriteRecipes, ::onRecipesFetched)
            failure(failure, ::showError)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_fav_recipes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipesAdapter
        }

        viewModel.getFavoriteRecipes()
    }

    private fun onRecipesFetched(recipes: List<RecipeView>?) {
        progress_fav_recipes.gone()
        if (recipes != null && recipes.isNotEmpty()) {
            val items = recipes.map { recipeView ->
                RecipeItem(recipeView,
                        clickListenerRecipe = { recipe ->
                            if (recipe.href.isNotEmpty()) {
                                val navDirections =
                                        FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToRecipeDetailsFragment().apply {
                                            href = recipe.href
                                            name = recipe.title
                                        }
                                findNavController().navigate(navDirections)
                            } else {
                                Snackbar.make(fav_recipes_root, R.string.recipe_details_no_href, Snackbar.LENGTH_LONG)
                                        .show()
                            }
                        },
                        clickListenerFav = { recipe, isFavorite ->
                            if (isFavorite) {
                                viewModel.saveRecipe(recipe)
                            } else {
                                viewModel.deleteRecipe(recipe.href)
                            }
                        })
            }
            recipesAdapter.clear()
            recipesAdapter.addAll(items)
            recycler_fav_recipes.visible()

        } else {
            Toast.makeText(context, getString(R.string.recipes_no_fav_results), Toast.LENGTH_LONG).show()
        }
    }

    private fun showError(failure: Failure?) {
        progress_fav_recipes.gone()
        when (failure) {
            is Failure.DbGetFavoriteRecipesError -> {
                Timber.tag(TAG).d("DbGetFavoriteRecipesError: ${failure.exception.message}")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                fragmentManager?.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}