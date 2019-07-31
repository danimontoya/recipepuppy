package com.recipepuppy.features.domain.usecase

import com.recipepuppy.core.interactor.UseCase
import com.recipepuppy.features.data.repository.RecipesRepository
import com.recipepuppy.features.domain.model.Recipe
import javax.inject.Inject

/**
 * Created by danieh on 30/07/2019.
 */
class GetFavoriteRecipesUseCase @Inject constructor(private val recipesRepository: RecipesRepository) :
    UseCase<List<Recipe>, UseCase.None>() {

    override suspend fun run(params: None) = recipesRepository.favoriteRecipes()
}