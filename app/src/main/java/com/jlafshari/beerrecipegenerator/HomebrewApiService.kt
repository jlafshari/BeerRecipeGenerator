package com.jlafshari.beerrecipegenerator

import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipegenerator.recipes.RecipeApi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class HomebrewApiService @Inject constructor() {
    private val baseUrl = "http://10.0.2.2:5000"
    private var recipeApi: RecipeApi

    init {
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        recipeApi = retrofit.create(RecipeApi::class.java)
    }

    fun getAllRecipePreviews(accessToken: String?) : Call<List<RecipePreview>> {
        val authHeader = getAuthHeader(accessToken)
        return recipeApi.getAllRecipePreviews(authHeader)
    }

    private fun getAuthHeader(accessToken: String?): String {
        return "Bearer $accessToken"
    }
}