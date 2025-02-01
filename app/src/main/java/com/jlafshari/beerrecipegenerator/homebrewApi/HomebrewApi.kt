package com.jlafshari.beerrecipegenerator.homebrewApi

import com.jlafshari.beerrecipecore.Fermentable
import com.jlafshari.beerrecipecore.Hop
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.RecipeUpdateInfo
import com.jlafshari.beerrecipecore.Style
import com.jlafshari.beerrecipecore.batches.Batch
import com.jlafshari.beerrecipecore.batches.BatchUpdateInfo
import com.jlafshari.beerrecipecore.recipes.Recipe
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipegenerator.settings.RecipeDefaultSettings
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

interface HomebrewApi {
    @GET("Recipe/GetAll")
    fun getAllRecipePreviews(
        @Header("Authorization") authHeader: String,
        @Query("abvMin") abvMin: String?, @Query("abvMax") abvMax: String?,
        @Query("colorMin") colorMin: String?, @Query("colorMax") colorMax: String?,
        @Query("yeastType") yeastType: String?,
        @Query("fermentableIds") fermentableIds: List<String>,
        @Query("hopIds") hopIds: List<String>
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

    @GET("Hop/{hopId}")
    fun getHopDetails(@Header("Authorization") authHeader: String, @Path("hopId") hopId: String): Single<Hop>

    @GET("Fermentable/{fermentableId}")
    fun getFermentableDetails(@Header("Authorization") authHeader: String, @Path("fermentableId") fermentableId: String):
            Single<Fermentable>

    @GET("Fermentable/GetAll")
    fun getAllFermentables(@Header("Authorization") authHeader: String): Single<List<Fermentable>>

    @GET("Hop/GetAll")
    fun getAllHops(@Header("Authorization") authHeader: String): Single<List<Hop>>

    @GET("Style/GetAll")
    fun getAllStyles(@Header("Authorization") authHeader: String): Single<List<Style>>

    @GET("UserSettings/GetSettings")
    fun getRecipeDefaultSettings(@Header("Authorization") authHeader: String): Single<RecipeDefaultSettings>

    @GET("Batch/{batchId}")
    fun getBatchDetails(@Header("Authorization") authHeader: String, @Path("batchId") batchId: String):
            Single<Batch>

    @PATCH("Batch/{batchId}")
    fun updateBatch(@Header("Authorization") authHeader: String, @Path("batchId") batchId: String,
                    @Body batchUpdateInfo: BatchUpdateInfo): Completable
}