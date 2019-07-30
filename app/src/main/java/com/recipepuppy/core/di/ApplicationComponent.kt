package com.recipepuppy.core.di

import com.recipepuppy.RecipePuppyApp
import com.recipepuppy.core.di.viewmodel.ViewModelModule
import com.recipepuppy.features.presentation.MainActivity
import com.recipepuppy.features.presentation.recipedetails.RecipeDetailsFragment
import com.recipepuppy.features.presentation.recipes.RecipesFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by danieh on 29/07/2019.
 */
@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class, DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(application: RecipePuppyApp)

    fun inject(mainActivity: MainActivity)

    fun inject(recipesFragment: RecipesFragment)

    fun inject(recipeDetailsFragment: RecipeDetailsFragment)
}
