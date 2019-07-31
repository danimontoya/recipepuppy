package com.recipepuppy.features.domain.usecase

import com.recipepuppy.core.interactor.UseCase
import com.recipepuppy.features.data.repository.RecipesRepository
import javax.inject.Inject

/**
 * Created by danieh on 30/07/2019.
 */
class DeleteRecipeUseCase @Inject constructor(private val recipesRepository: RecipesRepository) :
        UseCase<Boolean, DeleteRecipeUseCase.Params>() {

    override suspend fun run(params: Params) = recipesRepository.deleteRecipe(params.href)

    data class Params(val href: String)
}