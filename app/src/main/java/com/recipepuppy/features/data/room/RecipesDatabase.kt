package com.recipepuppy.features.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.recipepuppy.features.data.room.dao.RecipeInfoDao
import com.recipepuppy.features.data.room.entity.RecipeInfo

/**
 * Created by danieh on 30/07/2019.
 */
@Database(entities = [RecipeInfo::class], version = 1, exportSchema = false)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipeInfoDao(): RecipeInfoDao
}
