package com.recipepuppy.features.presentation.recipes

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recipepuppy.R
import com.recipepuppy.core.exception.Failure
import com.recipepuppy.core.extension.*
import com.recipepuppy.core.platform.BaseFragment
import com.recipepuppy.features.presentation.model.RecipeView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_recipes.*
import timber.log.Timber

/**
 * Created by danieh on 29/07/2019.
 */
class RecipesFragment : BaseFragment(), SearchView.OnQueryTextListener, View.OnFocusChangeListener {

    companion object {
        private val TAG = RecipesFragment::class.java.simpleName
        private const val ITEMS_BEFORE_FETCHING_NEXT_PAGE = 3
    }

    override fun layoutId() = R.layout.fragment_recipes

    private lateinit var viewModel: RecipesViewModel

    private val recipesAdapter = GroupAdapter<ViewHolder>()

    private lateinit var searchView: SearchView

    private var isSearching = false

    private lateinit var scrollListener: RecyclerView.OnScrollListener

    private var loadingNewItems = false

    private var searchingItems = false

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

        viewModel.getRecipes("onions,garlic")
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

            if (recipesAdapter.itemCount == 0) {
                scrollListener = object : RecyclerView.OnScrollListener() {

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                        Timber.tag(TAG).d("ItemCount=${recipesAdapter.itemCount}, lastVisibleItemPosition=$lastVisibleItemPosition")
                        val isTimeToLoadMoreItems = ITEMS_BEFORE_FETCHING_NEXT_PAGE >= recipesAdapter.itemCount - lastVisibleItemPosition
                        if (!loadingNewItems && isTimeToLoadMoreItems && !searchingItems) {
                            loadingNewItems = true
                            recipesAdapter.add(recipesAdapter.itemCount, LoadingItem())
                            viewModel.getRecipesNextPage()
                        }
                    }
                }
                recycler_recipes.addOnScrollListener(scrollListener)
            }

            val items = recipes.map { recipeView ->
                RecipeItem(recipeView, clickListenerRecipe = { _ ->
                    Toast.makeText(context, "Recipe clicked!", Toast.LENGTH_SHORT).show()
                }, clickListenerFav = { _, _ ->
                })
            }
            if (loadingNewItems) {
                loadingNewItems = false
                recipesAdapter.removeGroup(recipesAdapter.itemCount - 1)
            }
            recipesAdapter.addAll(items)
            recycler_recipes.visible()

        } else if (recipesAdapter.itemCount > 0) {
            Toast.makeText(context, getString(R.string.recipes_no_more_recipes), Toast.LENGTH_LONG).show()
            removeScrollListener()
        } else {
            Toast.makeText(context, getString(R.string.recipes_no_results), Toast.LENGTH_LONG).show()
            removeScrollListener()
        }
    }

    private fun removeScrollListener() {
        recycler_recipes.removeOnScrollListener(scrollListener)
    }

    private fun showError(failure: Failure?) {
        progress_recipes.gone()
        when (failure) {
            is Failure.ServerError -> {
                notify("ServerError")
                if (loadingNewItems) {
                    loadingNewItems = false
                    recipesAdapter.removeGroup(recipesAdapter.itemCount - 1)
                    removeScrollListener()
                }
            }
        }
    }
}