package com.recipepuppy.features.presentation.favoriterecipes

import arrow.core.Either
import com.nhaarman.mockito_kotlin.any
import com.recipepuppy.AndroidTest
import com.recipepuppy.features.domain.model.Recipe
import com.recipepuppy.features.domain.usecase.DeleteRecipeUseCase
import com.recipepuppy.features.domain.usecase.GetFavoriteRecipesUseCase
import com.recipepuppy.features.domain.usecase.SaveRecipeUseCase
import com.recipepuppy.features.presentation.model.RecipeView
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.notification.Failure
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

/**
 * Created by danieh on 01/08/2019.
 */
class FavoriteRecipesViewModelTest : AndroidTest() {

    @Mock
    lateinit var getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase
    @Mock
    lateinit var saveRecipeUseCase: SaveRecipeUseCase
    @Mock
    lateinit var deleteRecipeUseCase: DeleteRecipeUseCase

    private lateinit var favoriteRecipesViewModel: FavoriteRecipesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        favoriteRecipesViewModel =
            FavoriteRecipesViewModel(getFavoriteRecipesUseCase, saveRecipeUseCase, deleteRecipeUseCase)
    }

    @Test
    fun `fetch fav recipes,result should be the fetched all recipes with isFav=true`() {

        // Given or Arrange
        val favRecipes = listOf(
            Recipe("href3", "ingredient1,ingredient2", "thumbnail3", "title3"),
            Recipe("href5", "ingredient1,ingredient2", "thumbnail5", "title5")
        )
        val favRecipesResult = listOf(
            RecipeView("href3", "ingredient1,ingredient2", "thumbnail3", "title3", true),
            RecipeView("href5", "ingredient1,ingredient2", "thumbnail5", "title5", true)
        )
        Mockito.`when`(getFavoriteRecipesUseCase(any(), any())).thenAnswer { answer ->
            answer.getArgument<(Either<Failure, List<Recipe>>) -> Unit>(1)(Either.Right(favRecipes))
        }

        // Then or Assert
        favoriteRecipesViewModel.favoriteRecipes.observeForever {
            assertEquals(it!!.size, 2)
            assertEquals(it[0].href, "href3")
            assertEquals(it[0].title, "title3")
            assertEquals(it[0].isFavorite, true)
            assertEquals(it[1].href, "href5")
            assertEquals(it[1].title, "title5")
            assertEquals(it[1].isFavorite, true)
            it shouldEqual favRecipesResult
        }

        // When or Act
        runBlocking { favoriteRecipesViewModel.getFavoriteRecipes() }
    }

}