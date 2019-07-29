package com.recipepuppy.features.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by danieh on 29/07/2019.
 */
@Parcelize
data class RecipeView(
    val href: String,
    val ingredients: String,
    val thumbnail: String,
    val title: String
) : Parcelable