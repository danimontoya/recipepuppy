package com.recipepuppy.features.domain.usecase

import arrow.core.Either
import com.recipepuppy.UnitTest
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.recipepuppy.core.exception.Failure
import com.recipepuppy.features.data.repository.RecipesRepository
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

/**
 * Created by danieh on 01/08/2019.
 */
class DeleteRecipeUseCaseTest : UnitTest() {

    private lateinit var deleteRecipeUseCase: DeleteRecipeUseCase

    @Mock
    private lateinit var recipesRepository: RecipesRepository

    @Before
    fun setUp() {
        deleteRecipeUseCase = DeleteRecipeUseCase(recipesRepository)
    }

    @Test
    fun `delete chosen recipe and return true when it goes well`() {
        val recipeHref = "href3"
        given { recipesRepository.deleteRecipe(recipeHref) }.willReturn(Either.Right(true))

        var result: Either<Failure, Boolean>? = null
        runBlocking {
            result = deleteRecipeUseCase.run(DeleteRecipeUseCase.Params(recipeHref))
            result
        }

        verify(recipesRepository).deleteRecipe(recipeHref)
        result shouldEqual Either.Right(true)
        verifyNoMoreInteractions(recipesRepository)
    }
}
