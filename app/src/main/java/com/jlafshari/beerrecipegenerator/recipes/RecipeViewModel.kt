package com.jlafshari.beerrecipegenerator.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipegenerator.HomebrewApiService
import com.jlafshari.beerrecipegenerator.ui.login.AzureAuthHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val homebrewApiService: HomebrewApiService) : ViewModel() {
    private val _loadRecipePreviewsResponse = MutableLiveData<List<RecipePreview>>()
    val loadRecipePreviewsResponse: LiveData<List<RecipePreview>> = _loadRecipePreviewsResponse
    private val _loadAccessTokenResponse = MutableLiveData<String?>()
    val loadAccessTokenResponse: LiveData<String?> = _loadAccessTokenResponse

    fun loadRecipePreviews() {
        val call = homebrewApiService.getAllRecipePreviews(loadAccessTokenResponse.value)
        call.enqueue(object : Callback<List<RecipePreview>> {
            override fun onResponse(
                call: Call<List<RecipePreview>>,
                response: Response<List<RecipePreview>>
            ) {
                if (response.isSuccessful) {
                    val recipes = response.body()
                    if (recipes != null) {
                        _loadRecipePreviewsResponse.postValue(recipes!!)
                    }
                }
            }

            override fun onFailure(call: Call<List<RecipePreview>>, t: Throwable) { }
        })
    }

    fun loadAccessToken() {
        AzureAuthHelper.getAccessTokenAsync { _loadAccessTokenResponse.postValue(it) }
    }
}