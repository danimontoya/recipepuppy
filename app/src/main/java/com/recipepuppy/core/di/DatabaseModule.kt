package com.recipepuppy.core.di

import android.app.Application
import androidx.room.Room
import com.recipepuppy.features.data.room.RecipesDatabase
import com.recipepuppy.features.data.room.dao.RecipeInfoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by danieh on 30/07/2019.
 */
@Module
class DatabaseModule(application: Application) {

    companion object {
        private const val RECIPES_DB = "recipes_db"
    }

    private var database: RecipesDatabase =
        Room.databaseBuilder(application, RecipesDatabase::class.java, RECIPES_DB).build()

    @Singleton
    @Provides
    fun providesRecipesDatabase(): RecipesDatabase {
        return database
    }

    @Singleton
    @Provides
    fun providesRecipeInfoDao(recipesDatabase: RecipesDatabase): RecipeInfoDao {
        return recipesDatabase.recipeInfoDao()
    }
}