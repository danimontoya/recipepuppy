package com.recipepuppy.features.domain.usecase

import arrow.core.Either
import com.recipepuppy.UnitTest
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.recipepuppy.core.exception.Failure
import com.recipepuppy.features.data.repository.RecipesRepository
import com.recipepuppy.features.domain.model.Recipe
import com.recipepuppy.features.domain.model.RecipesResult
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

/**
 * Created by danieh on 01/08/2019.
 */
class GetRecipesUseCaseTest : UnitTest() {

    private lateinit var getRecipesUseCase: GetRecipesUseCase

    @Mock
    private lateinit var recipesRepository: RecipesRepository

    @Before
    fun setUp() {
        getRecipesUseCase = GetRecipesUseCase(recipesRepository)
    }

    @Test
    fun `should get data from repository (recipes and fav recipes) and wrap them in recipesResult`() {
        val currentIngredients = "ingredient1,ingredient2"
        val currentPage = 1
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

        given { recipesRepository.recipes(currentIngredients, currentPage) }.willReturn(Either.Right(recipes))
        given { recipesRepository.favoriteRecipes() }.willReturn(Either.Right(favRecipes))

        var result: Either<Failure, RecipesResult>? = null
        runBlocking {
            result = getRecipesUseCase.run(GetRecipesUseCase.Params(currentIngredients, currentPage))
            result
        }

        verify(recipesRepository).recipes(currentIngredients, currentPage)
        verify(recipesRepository).favoriteRecipes()
        result shouldEqual Either.Right(recipesResult)
        verifyNoMoreInteractions(recipesRepository)
    }
}
