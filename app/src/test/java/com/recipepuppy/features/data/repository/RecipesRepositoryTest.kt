package com.recipepuppy.features.data.repository

import arrow.core.Either
import com.recipepuppy.UnitTest
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.recipepuppy.core.exception.Failure
import com.recipepuppy.core.platform.NetworkHandler
import com.recipepuppy.features.data.datasource.RecipesService
import com.recipepuppy.features.data.model.RecipeEntity
import com.recipepuppy.features.data.model.RecipesEntity
import com.recipepuppy.features.data.room.dao.RecipeInfoDao
import com.recipepuppy.features.data.room.entity.RecipeInfo
import com.recipepuppy.features.domain.model.Recipe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.Call
import retrofit2.Response

/**
 * Created by danieh on 01/08/2019.
 */
class RecipesRepositoryTest : UnitTest() {

    private lateinit var repository: RecipesRepository.Network

    @Mock
    private lateinit var networkHandler: NetworkHandler
    @Mock
    private lateinit var service: RecipesService

    @Mock
    private lateinit var recipesCall: Call<RecipesEntity>
    @Mock
    private lateinit var recipesResponse: Response<RecipesEntity>
    @Mock
    private lateinit var recipeInfoDao: RecipeInfoDao

    private var ingredients = "onions,cheese"
    private var page = 1
    private var mapArgs = mapOf("i" to ingredients, "p" to page.toString())

    @Before
    fun setUp() {
        repository = RecipesRepository.Network(networkHandler, service, recipeInfoDao)
    }

    @Test
    fun `recipes call should return a failure if body is null`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { recipesResponse.body() }.willReturn(null)
        given { recipesResponse.isSuccessful }.willReturn(true)
        given { recipesCall.execute() }.willReturn(recipesResponse)
        given { service.recipes(mapArgs) }.willReturn(recipesCall)

        val recipes = repository.recipes(ingredients, page)

        recipes.isLeft() shouldEqual true
        recipes.fold({ failure -> failure shouldBeInstanceOf Failure.BodyNullError::class.java }, {})
        verify(service).recipes(mapArgs)
    }

    @Test
    fun `should get post list from service`() {

        val recipeEntityList = emptyList<RecipeEntity>()
        val recipesEntity = RecipesEntity("", recipeEntityList, "title", 1.0)

        given { networkHandler.isConnected }.willReturn(true)
        given { recipesResponse.body() }.willReturn(recipesEntity)
        given { recipesResponse.isSuccessful }.willReturn(true)
        given { recipesCall.execute() }.willReturn(recipesResponse)
        given { service.recipes(mapArgs) }.willReturn(recipesCall)

        val recipes = repository.recipes(ingredients, page)

        recipes shouldEqual Either.Right(recipeEntityList)
        verify(service).recipes(mapArgs)
    }

    @Test
    fun `recipes service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val recipes = repository.recipes(ingredients, page)

        recipes shouldBeInstanceOf Either::class.java
        recipes.isLeft() shouldEqual true
        recipes.fold({ failure -> failure shouldBeInstanceOf Failure.NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `recipes service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val recipes = repository.recipes(ingredients, page)

        recipes shouldBeInstanceOf Either::class.java
        recipes.isLeft() shouldEqual true
        recipes.fold({ failure -> failure shouldBeInstanceOf Failure.NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test
    fun `recipes service should return server error if no successful response`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { recipesResponse.isSuccessful }.willReturn(false)
        given { recipesCall.execute() }.willReturn(recipesResponse)
        given { service.recipes(mapArgs) }.willReturn(recipesCall)

        val recipes = repository.recipes(ingredients, page)

        recipes shouldBeInstanceOf Either::class.java
        recipes.isLeft() shouldEqual true
        recipes.fold({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }

    @Test
    fun `recipes request should catch exceptions`() {
        given { networkHandler.isConnected }.willReturn(true)

        val recipes = repository.recipes(ingredients, page)

        recipes shouldBeInstanceOf Either::class.java
        recipes.isLeft() shouldEqual true
        recipes.fold({ failure -> failure shouldBeInstanceOf Failure.ServerError::class.java }, {})
    }

    @Test
    fun `save recipe into database, should return true`() {
        val recipe = Recipe("href3", "ingredient1,ingredient2", "thumbnail3", "title3")
        given { recipeInfoDao.insert(recipe.toRecipeInfo()) }.willReturn(1)

        val result = repository.saveRecipe(recipe)

        result shouldBeInstanceOf Either::class.java
        result.isRight() shouldEqual true
        result shouldEqual Either.Right(true)
        verify(recipeInfoDao).insert(recipe.toRecipeInfo())
    }

    @Test
    fun `delete recipe from database, should return true`() {
        val recipeHref = "href3"
        given { recipeInfoDao.delete(recipeHref) }.willReturn(1)

        val result = repository.deleteRecipe(recipeHref)

        result shouldBeInstanceOf Either::class.java
        result.isRight() shouldEqual true
        result shouldEqual Either.Right(true)
        verify(recipeInfoDao).delete(recipeHref)
    }

    @Test
    fun `get all fav recipes from database, should return list of recipes`() {
        val favRecipes = listOf(
            RecipeInfo("href1", "ingredient1,ingredient2", "thumbnail1", "title1"),
            RecipeInfo("href2", "ingredient1,ingredient2", "thumbnail2", "title2"),
            RecipeInfo("href3", "ingredient1,ingredient2", "thumbnail3", "title3"),
            RecipeInfo("href4", "ingredient1,ingredient2", "thumbnail4", "title4")
        )
        val resultFavRecipes = favRecipes.map { it.toRecipe() }
        given { recipeInfoDao.favoriteRecipes }.willReturn(favRecipes)

        val result = repository.favoriteRecipes()

        result shouldBeInstanceOf Either::class.java
        result.isRight() shouldEqual true
        result shouldEqual Either.Right(resultFavRecipes)
        verify(recipeInfoDao).favoriteRecipes
    }

}