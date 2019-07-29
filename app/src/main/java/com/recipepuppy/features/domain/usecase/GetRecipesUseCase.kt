package com.recipepuppy.features.domain.usecase

import com.recipepuppy.core.interactor.UseCase
import com.recipepuppy.features.data.repository.RecipesRepository
import com.recipepuppy.features.domain.model.Recipe
import javax.inject.Inject

/**
 * Created by danieh on 29/07/2019.
 */
class GetRecipesUseCase @Inject constructor(private val recipesRepository: RecipesRepository) :
    UseCase<List<Recipe>, GetRecipesUseCase.Params>() {

    override suspend fun run(params: Params) = recipesRepository.recipes(params.ingredients, params.query, params.page)

    data class Params(val ingredients: String, val query: String, val page: Int)
}