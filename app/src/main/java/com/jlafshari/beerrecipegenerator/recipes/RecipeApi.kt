package com.jlafshari.beerrecipegenerator.recipes

import com.jlafshari.beerrecipecore.recipes.RecipePreview
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header

interface RecipeApi {
    @GET("Recipe/GetAll")
    fun getAllRecipePreviews(@Header("Authorization") authHeader: String): Single<List<RecipePreview>>
}