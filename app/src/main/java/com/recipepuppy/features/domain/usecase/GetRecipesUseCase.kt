package com.recipepuppy.features.domain.usecase

import arrow.core.Either
import arrow.core.getOrElse
import com.recipepuppy.core.exception.Failure
import com.recipepuppy.core.interactor.UseCase
import com.recipepuppy.features.data.repository.RecipesRepository
import com.recipepuppy.features.domain.model.Recipe
import com.recipepuppy.features.domain.model.RecipesResult
import javax.inject.Inject

/**
 * Created by danieh on 29/07/2019.
 */
class GetRecipesUseCase @Inject constructor(private val recipesRepository: RecipesRepository) :
        UseCase<RecipesResult, GetRecipesUseCase.Params>() {

    override suspend fun run(params: Params) = zip(recipesRepository.recipes(params.ingredients, params.page), recipesRepository.favoriteRecipes())

    private fun zip(recipes: Either<Failure, List<Recipe>>, favoriteRecipes: Either<Failure, List<Recipe>>): Either<Failure, RecipesResult> {

        return if (recipes.isRight() && favoriteRecipes.isRight()) {
            val recipesResult = RecipesResult(recipes.getOrElse { emptyList() }, favoriteRecipes.getOrElse { emptyList() })
            Either.right(recipesResult)

        } else {
            Either.left(Failure.GetRecipesError())
        }
    }

    data class Params(val ingredients: String, val page: Int)
}