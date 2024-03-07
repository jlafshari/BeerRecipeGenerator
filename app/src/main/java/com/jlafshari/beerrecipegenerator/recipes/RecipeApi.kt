package com.jlafshari.beerrecipegenerator.recipes

import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.RecipeUpdateInfo
import com.jlafshari.beerrecipecore.recipes.Recipe
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("Recipe/GetAll")
    fun getAllRecipePreviews(@Header("Authorization") authHeader: String,
                             @Query("abvMin") abvMin: String?, @Query("abvMax") abvMax: String?,
                             @Query("colorMin") colorMin: String?, @Query("colorMax") colorMax: String?,
                             @Query("yeastType") yeastType: String?
    ): Single<List<RecipePreview>>

    @GET("Recipe/{recipeId}")
    fun getRecipeDetails(@Header("Authorization") authHeader: String, @Path("recipeId") recipeId: String):
            Single<Recipe>

    @PATCH("Recipe/{recipeId}")
    fun updateRecipe(@Header("Authorization") authHeader: String, @Path("recipeId") recipeId: String,
                     @Body recipeUpdateInfo: RecipeUpdateInfo): Completable

    @DELETE("Recipe/{recipeId}")
    fun deleteRecipe(@Header("Authorization") authHeader: String, @Path("recipeId") recipeId: String): Completable

    @POST("Recipe/GenerateRecipe")
    fun generateRecipe(@Header("Authorization") authHeader: String, @Body recipeGenerationInfo: RecipeGenerationInfo):
            Single<Recipe>
}