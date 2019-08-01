package com.recipepuppy.features.presentation.recipes

import arrow.core.Either
import com.nhaarman.mockito_kotlin.any
import com.recipepuppy.AndroidTest
import com.recipepuppy.features.domain.model.Recipe
import com.recipepuppy.features.domain.model.RecipesResult
import com.recipepuppy.features.domain.usecase.DeleteRecipeUseCase
import com.recipepuppy.features.domain.usecase.GetFavoriteRecipesUseCase
import com.recipepuppy.features.domain.usecase.GetRecipesUseCase
import com.recipepuppy.features.domain.usecase.SaveRecipeUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.notification.Failure
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals as assertEquals1

/**
 * Created by danieh on 01/08/2019.
 */
class RecipesViewModelTest : AndroidTest() {

    @Mock
    lateinit var getRecipesUseCase: GetRecipesUseCase
    @Mock
    lateinit var getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase
    @Mock
    lateinit var saveRecipeUseCase: SaveRecipeUseCase
    @Mock
    lateinit var deleteRecipeUseCase: DeleteRecipeUseCase

    private lateinit var recipesViewModel: RecipesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        recipesViewModel =
            RecipesViewModel(getRecipesUseCase, getFavoriteRecipesUseCase, saveRecipeUseCase, deleteRecipeUseCase)
    }

    @Test
    fun `fetch recipes based on (ingredients, page) and fetch fav recipes,result should be the fetched recipes with isFav=true if it was saved`() {

        // Given or Arrange
        val currentIngredients = "ingredient1,ingredient2"
        val recipes = listOf(
            Recipe("href1", currentIngredients, "thumbnail1", "title1"),
            Recipe("href2", currentIngredients, "thumbnail2", "title2"),
            Recipe("href3", currentIngredients, "thumbnail3", "title3"),
            Recipe("href4", currentIngredients, "thumbnail4", "title4")
        )
        val favRecipes = listOf(
            Recipe("href3", "ingredient1,ingredient2", "thumbnail3", "title3"),
            Recipe("href5", "ingredient1,ingredient2", "thumbnail5", "title5")
        )
        val recipesResult = RecipesResult(recipes, favRecipes)
        Mockito.`when`(getRecipesUseCase(any(), any())).thenAnswer { answer ->
            answer.getArgument<(Either<Failure, RecipesResult>) -> Unit>(1)(Either.Right(recipesResult))
        }

        // Then or Assert
        recipesViewModel.recipeList.observeForever {
            assertEquals1(it!!.size, 4)
            assertEquals1(it[0].href, "href1")
            assertEquals1(it[0].title, "title1")
            assertEquals1(it[1].href, "href2")
            assertEquals1(it[1].title, "title2")
            assertEquals1(it[1].isFavorite, false)
            assertEquals1(it[2].isFavorite, true)
        }

        // When or Act
        runBlocking { recipesViewModel.getRecipes(currentIngredients) }
    }
}