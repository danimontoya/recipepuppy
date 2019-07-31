package com.recipepuppy.features.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.recipepuppy.features.data.room.entity.RecipeInfo

/**
 * Created by danieh on 30/07/2019.
 */
@Dao
interface RecipeInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipeInfo: RecipeInfo): Long

    @Query("DELETE FROM recipes WHERE href = :href")
    fun delete(href: String): Int

    @get:Query("SELECT * FROM recipes")
    val favoriteRecipes: List<RecipeInfo>
}