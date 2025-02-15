package com.jlafshari.beerrecipegenerator.recipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.RecipeUpdateInfo
import com.jlafshari.beerrecipecore.recipes.Recipe
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipegenerator.BaseViewModel
import com.jlafshari.beerrecipegenerator.RecipeSearchFilter
import com.jlafshari.beerrecipegenerator.homebrewApi.ApiResponse
import com.jlafshari.beerrecipegenerator.homebrewApi.HomebrewApiService
import com.jlafshari.beerrecipegenerator.settings.RecipeDefaultSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val homebrewApiService: HomebrewApiService) : BaseViewModel() {

    private val _loadRecipePreviewsResponse = MutableLiveData<List<RecipePreview>>()
    val loadRecipePreviewsResponse: LiveData<List<RecipePreview>> = _loadRecipePreviewsResponse

    private val _loadRecipeDetailsResponse = MutableLiveData<Recipe?>()
    val loadRecipeDetailsResponse: LiveData<Recipe?> = _loadRecipeDetailsResponse

    private val _generateRecipeResponse = MutableLiveData<Recipe>()
    val generateRecipeResponse: LiveData<Recipe> = _generateRecipeResponse

    private val _updateRecipeResponse = MutableLiveData<ApiResponse>()
    val updateRecipeResponse: LiveData<ApiResponse> = _updateRecipeResponse

    private val _deleteRecipeResponse = MutableLiveData<ApiResponse>()
    val deleteRecipeResponse: LiveData<ApiResponse> = _deleteRecipeResponse

    private val _loadRecipeDefaultSettingsResponse = MutableLiveData<RecipeDefaultSettings>()
    val loadRecipeDefaultSettingsResponse: LiveData<RecipeDefaultSettings> = _loadRecipeDefaultSettingsResponse

    fun loadRecipePreviews(
        recipeSearchFilter: RecipeSearchFilter,
        onError: () -> Unit
    ) {
        runIfTokenIsValid {
            homebrewApiService.getAllRecipePreviews(authResult!!.authorizationHeader, recipeSearchFilter)
                .toObservable()
                .retryWhen(retryWithDelay(10, 2))
                .subscribeThenDispose(
                    { _loadRecipePreviewsResponse.postValue(it) },
                    {
                        Log.d("", "load recipe previews error ", it)
                        onError()
                    }
                )
        }
    }

    fun loadRecipeDetails(recipeId: String) {
        runIfTokenIsValid {
            homebrewApiService.getRecipeDetails(authResult!!.authorizationHeader, recipeId)
                .subscribeThenDispose(
                    { _loadRecipeDetailsResponse.postValue(it) },
                    { Log.d("", "load recipe details error", it) }
                )
        }
    }

    fun loadRecipeDetailsComplete() {
        _loadRecipeDetailsResponse.postValue(null)
    }

    fun generateRecipe(recipeGenerationInfo: RecipeGenerationInfo) {
        runIfTokenIsValid {
            homebrewApiService.generateRecipe(authResult!!.authorizationHeader, recipeGenerationInfo)
                .subscribeThenDispose(
                    { _generateRecipeResponse.postValue(it) },
                    { Log.d("", "generate recipe error", it) }
                )
        }
    }

    fun updateRecipe(recipeId: String, recipeUpdateInfo: RecipeUpdateInfo) {
        runIfTokenIsValid {
            homebrewApiService.updateRecipe(authResult!!.authorizationHeader, recipeId, recipeUpdateInfo)
                .subscribeThenDispose({
                        _updateRecipeResponse.postValue(ApiResponse(true))
                    },
                    {
                        Log.d("", "update recipe error", it)
                        _updateRecipeResponse.postValue(ApiResponse(false))
                    }
                )
        }
    }

    fun deleteRecipe(recipeId: String) {
        runIfTokenIsValid {
            homebrewApiService.deleteRecipe(authResult!!.authorizationHeader, recipeId)
                .subscribeThenDispose({
                    _deleteRecipeResponse.postValue(ApiResponse(true))
                    },
                    {
                        Log.d("", "delete recipe error", it)
                        _deleteRecipeResponse.postValue(ApiResponse(false))
                    }
                )
        }
    }

    fun loadRecipeDefaultSettings() {
        runIfTokenIsValid {
            homebrewApiService.getRecipeDefaultSettings(authResult!!.authorizationHeader)
                .subscribeThenDispose(
                    { _loadRecipeDefaultSettingsResponse.postValue(it) },
                    { Log.d("", "load recipe default settings error", it) }
                )
        }
    }
}