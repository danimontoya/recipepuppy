package com.recipepuppy.features.data.datasource

import com.recipepuppy.features.data.model.RecipesEntity
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by danieh on 29/07/2019.
 */
@Singleton
class RecipesService @Inject constructor(retrofit: Retrofit) : Api {

    private val api by lazy { retrofit.create(Api::class.java) }

    override fun recipes(filters: Map<String, String>): Call<RecipesEntity> = api.recipes(filters)
}