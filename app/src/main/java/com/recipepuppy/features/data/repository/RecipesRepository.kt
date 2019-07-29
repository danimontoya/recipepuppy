package com.recipepuppy.features.data.repository

import arrow.core.Either
import com.recipepuppy.core.exception.Failure
import com.recipepuppy.core.platform.NetworkHandler
import com.recipepuppy.features.data.datasource.RecipesService
import com.recipepuppy.features.domain.model.Recipe
import retrofit2.Call
import javax.inject.Inject

/**
 * Created by danieh on 29/07/2019.
 */
interface RecipesRepository {

    fun recipes(ingredients: String, query: String, page: Int): Either<Failure, List<Recipe>>

    class Network @Inject constructor(private val networkHandler: NetworkHandler, private val service: RecipesService) :
        RecipesRepository {

        override fun recipes(ingredients: String, query: String, page: Int): Either<Failure, List<Recipe>> {
            return when (networkHandler.isConnected) {
                true -> request(
                    service.recipes(
                        mapOf(
                            "i" to ingredients,
                            "q" to query,
                            "p" to page.toString()
                        )
                    )
                ) { recipesEntity -> recipesEntity.results.map { it.toRecipe() } }
                false, null -> Either.Left(Failure.NetworkConnection())
            }
        }

        private fun <T, R> request(call: Call<T>, transform: (T) -> R): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> {
                        response.body()?.let { Either.Right(transform(it)) }
                            ?: Either.Left(Failure.BodyNullError())
                    }
                    false -> Either.Left(Failure.ServerError(Throwable(response.errorBody()?.string())))
                }
            } catch (exception: Throwable) {
                Either.Left(Failure.ServerError(exception))
            }
        }
    }
}