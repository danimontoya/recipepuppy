package com.recipepuppy.features.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.recipepuppy.features.domain.model.Recipe

/**
 * Created by danieh on 30/07/2019.
 */
@Entity(tableName = "recipes")
data class RecipeInfo(
        @PrimaryKey
        @ColumnInfo(name = "href") var href: String,
        @ColumnInfo(name = "ingredients") var ingredients: String,
        @ColumnInfo(name = "thumbnail") var thumbnail: String,
        @ColumnInfo(name = "title") var title: String
) {
    fun toRecipe() = Recipe(href, ingredients, thumbnail, title)

    companion object {
        fun empty() = RecipeInfo("", "", "", "")
    }
}
