package com.recipepuppy.features.domain.usecase

import arrow.core.Either
import com.recipepuppy.UnitTest
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.recipepuppy.core.exception.Failure
import com.recipepuppy.features.data.repository.RecipesRepository
import com.recipepuppy.features.domain.model.Recipe
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

/**
 * Created by danieh on 01/08/2019.
 */
class SaveRecipeUseCaseTest : UnitTest() {

    private lateinit var saveRecipeUseCase: SaveRecipeUseCase

    @Mock
    private lateinit var recipesRepository: RecipesRepository

    @Before
    fun setUp() {
        saveRecipeUseCase = SaveRecipeUseCase(recipesRepository)
    }

    @Test
    fun `save chosen recipe and return true when it goes well`() {
        val recipe = Recipe("href3", "ingredient1,ingredient2", "thumbnail3", "title3")
        given { recipesRepository.saveRecipe(recipe) }.willReturn(Either.Right(true))

        var result: Either<Failure, Boolean>? = null
        runBlocking {
            result = saveRecipeUseCase.run(SaveRecipeUseCase.Params(recipe.toRecipeView()))
            result
        }

        verify(recipesRepository).saveRecipe(recipe)
        result shouldEqual Either.Right(true)
        verifyNoMoreInteractions(recipesRepository)
    }
}
