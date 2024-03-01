package com.jlafshari.beerrecipegenerator.recipes

import com.jlafshari.beerrecipecore.recipes.RecipePreview
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecipeApi {
    @GET("Recipe/GetAll")
    fun getAllRecipePreviews(@Header("Authorization") authHeader: String,
                             @Query("abvMin") abvMin: String?, @Query("abvMax") abvMax: String?,
                             @Query("colorMin") colorMin: String?, @Query("colorMax") colorMax: String?,
                             @Query("yeastType") yeastType: String?
    ): Single<List<RecipePreview>>
}