package com.recipepuppy.features.domain.usecase

import arrow.core.Either
import com.recipepuppy.UnitTest
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.recipepuppy.core.exception.Failure
import com.recipepuppy.core.interactor.UseCase
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
class GetFavoriteRecipesUseCaseTest : UnitTest() {

    private lateinit var getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase

    @Mock
    private lateinit var recipesRepository: RecipesRepository

    @Before
    fun setUp() {
        getFavoriteRecipesUseCase = GetFavoriteRecipesUseCase(recipesRepository)
    }

    @Test
    fun `should get data from repository (database) and return the list of fav recipes`() {
        val favRecipes = listOf(
            Recipe("href3", "ingredient1,ingredient2", "thumbnail3", "title3"),
            Recipe("href5", "ingredient1,ingredient2", "thumbnail5", "title5")
        )
        given { recipesRepository.favoriteRecipes() }.willReturn(Either.Right(favRecipes))

        var result: Either<Failure, List<Recipe>>? = null
        runBlocking {
            result = getFavoriteRecipesUseCase.run(UseCase.None())
            result
        }

        verify(recipesRepository).favoriteRecipes()
        result shouldEqual Either.Right(favRecipes)
        verifyNoMoreInteractions(recipesRepository)
    }
}
