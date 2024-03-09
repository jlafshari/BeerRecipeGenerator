package com.jlafshari.beerrecipegenerator

import com.jlafshari.beerrecipecore.Fermentable
import com.jlafshari.beerrecipecore.Hop
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.RecipeUpdateInfo
import com.jlafshari.beerrecipecore.recipes.Recipe
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipegenerator.recipes.RecipeApi
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class HomebrewApiService @Inject constructor() {
    private var recipeApi: RecipeApi

    init {
        val baseUrl = getBaseUrl()

        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        recipeApi = retrofit.create(RecipeApi::class.java)
    }

    private fun getBaseUrl(): String =
        "${BuildConfig.homebrewApiHttpScheme}://${BuildConfig.homebrewApiBaseDomain}"

    fun getAllRecipePreviews(authHeader: String, abvMin: String?, abvMax: String?,
                             colorMin: String?, colorMax: String?,
                             yeastType: String?
        ) : Single<List<RecipePreview>> {
        return recipeApi.getAllRecipePreviews(authHeader, abvMin, abvMax, colorMin, colorMax, yeastType)
    }

    fun getRecipeDetails(authHeader: String, recipeId: String): Single<Recipe> =
        recipeApi.getRecipeDetails(authHeader, recipeId)

    fun generateRecipe(authHeader: String, recipeGenerationInfo: RecipeGenerationInfo): Single<Recipe> =
        recipeApi.generateRecipe(authHeader, recipeGenerationInfo)

    fun updateRecipe(authHeader: String, recipeId: String, recipeUpdateInfo: RecipeUpdateInfo): Completable =
        recipeApi.updateRecipe(authHeader, recipeId, recipeUpdateInfo)

    fun deleteRecipe(authHeader: String, recipeId: String): Completable =
        recipeApi.deleteRecipe(authHeader, recipeId)

    fun getHopDetails(authHeader: String, hopId: String): Single<Hop> = recipeApi.getHopDetails(authHeader, hopId)

    fun getFermentableDetails(authHeader: String, fermentableId: String): Single<Fermentable> =
        recipeApi.getFermentableDetails(authHeader, fermentableId)

    fun getAllFermentables(authHeader: String): Single<List<Fermentable>> = recipeApi.getAllFermentables(authHeader)
}