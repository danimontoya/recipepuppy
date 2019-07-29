package com.recipepuppy.core.di

import com.recipepuppy.features.data.repository.RecipesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by danieh on 29/07/2019.
 */
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideRecipesRepository(dataSource: RecipesRepository.Network): RecipesRepository = dataSource
}
