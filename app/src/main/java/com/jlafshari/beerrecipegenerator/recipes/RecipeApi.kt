package com.jlafshari.beerrecipegenerator.recipes

import com.jlafshari.beerrecipecore.recipes.RecipePreview
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RecipeApi {
    @GET("Recipe/GetAll")
    fun getAllRecipePreviews(@Header("Authorization") authHeader: String): Call<List<RecipePreview>>
}