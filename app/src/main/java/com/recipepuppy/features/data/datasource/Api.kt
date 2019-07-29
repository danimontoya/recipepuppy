package com.recipepuppy.features.data.datasource

import com.recipepuppy.features.data.model.RecipesEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by danieh on 29/07/2019.
 */
interface Api {

    @GET("api/")
    fun recipes(@QueryMap(encoded=true) filters: Map<String, String>): Call<RecipesEntity>
}