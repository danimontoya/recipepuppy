package com.recipepuppy.features.presentation.recipes

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
class RecipesFragment : BaseFragment(), SearchView.OnQueryTextListener, View.OnFocusChangeListener {

    override fun layoutId() = R.layout.fragment_recipes

    private lateinit var viewModel: RecipesViewModel

    private val recipesAdapter = GroupAdapter<ViewHolder>()

    private lateinit var searchView: SearchView

    private var isSearching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setHasOptionsMenu(true)

        viewModel = viewModel(viewModelFactory) {
            observe(recipeList, ::onRecipesFetched)
            observe(restartSearch, ::onRestartSearch)
            failure(failure, ::showError)
        }
    }

    private fun onRestartSearch(restart: Boolean?) {
        restart?.let {
            if (restart) {
                recipesAdapter.clear()
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_recipes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipesAdapter
        }

        viewModel.getRecipes("")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.apply {
            setOnQueryTextListener(this@RecipesFragment)
            setOnQueryTextFocusChangeListener(this@RecipesFragment)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            viewModel.getRecipes(it)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            if (it.length > 2)
                viewModel.getRecipes(it)
        }
        return true
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (v?.id == searchView.id)
            isSearching = hasFocus
    }

    private fun onRecipesFetched(recipes: List<RecipeView>?) {
        progress_recipes.gone()
        if (recipes != null && recipes.isNotEmpty()) {
            val items = recipes.map { recipeView ->
                RecipeItem(recipeView, clickListenerRecipe = { _ ->
                    Toast.makeText(context, "Recipe clicked!", Toast.LENGTH_SHORT).show()
                }, clickListenerFav = { _, _ ->
                })
            }
            recipesAdapter.addAll(items)
            recycler_recipes.visible()
        } else if (recipesAdapter.itemCount > 0) {
            Toast.makeText(context, getString(R.string.recipes_no_more_recipes), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, getString(R.string.recipes_no_results), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError(failure: Failure?) {
        progress_recipes.gone()
        when (failure) {
            is Failure.ServerError -> notify("ServerError")
        }
    }
}