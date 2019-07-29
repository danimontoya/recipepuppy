package com.recipepuppy.features.data.datasource

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by danieh on 29/07/2019.
 */
@Singleton
class RecipesService @Inject constructor(retrofit: Retrofit) : Api {

    private val api by lazy { retrofit.create(Api::class.java) }
}