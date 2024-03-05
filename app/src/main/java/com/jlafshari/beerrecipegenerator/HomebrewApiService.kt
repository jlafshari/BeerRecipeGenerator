package com.jlafshari.beerrecipegenerator

import com.jlafshari.beerrecipecore.recipes.Recipe
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipegenerator.recipes.RecipeApi
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class HomebrewApiService @Inject constructor() {
    private val baseUrl = "http://10.0.2.2:5000"
    private var recipeApi: RecipeApi

    init {
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        recipeApi = retrofit.create(RecipeApi::class.java)
    }

    fun getAllRecipePreviews(authHeader: String, abvMin: String?, abvMax: String?,
                             colorMin: String?, colorMax: String?,
                             yeastType: String?
        ) : Single<List<RecipePreview>> {
        return recipeApi.getAllRecipePreviews(authHeader, abvMin, abvMax, colorMin, colorMax, yeastType)
    }

    fun getRecipeDetails(authHeader: String, recipeId: String): Single<Recipe> =
        recipeApi.getRecipeDetails(authHeader, recipeId)
}