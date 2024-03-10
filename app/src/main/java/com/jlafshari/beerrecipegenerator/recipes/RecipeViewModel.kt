package com.jlafshari.beerrecipegenerator.recipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.RecipeUpdateInfo
import com.jlafshari.beerrecipecore.recipes.Recipe
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipegenerator.ApiResponse
import com.jlafshari.beerrecipegenerator.BaseViewModel
import com.jlafshari.beerrecipegenerator.HomebrewApiService
import com.jlafshari.beerrecipegenerator.settings.RecipeDefaultSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    fun loadRecipePreviews(abvMin: String?, abvMax: String?,
                           colorMin: String?, colorMax: String?,
                           yeastType: String?) {
        runIfTokenIsValid {
            homebrewApiService.getAllRecipePreviews(authResult!!.authorizationHeader,
                abvMin, abvMax, colorMin, colorMax, yeastType
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _loadRecipePreviewsResponse.postValue(it) },
                    { Log.d("", "load recipe previews error ", it) }
                )
                .disposeWhenCleared()
        }
    }

    fun loadRecipeDetails(recipeId: String) {
        runIfTokenIsValid {
            homebrewApiService.getRecipeDetails(authResult!!.authorizationHeader, recipeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _loadRecipeDetailsResponse.postValue(it) },
                    { Log.d("", "load recipe details error", it) }
                )
                .disposeWhenCleared()
        }
    }

    fun loadRecipeDetailsComplete() {
        _loadRecipeDetailsResponse.postValue(null)
    }

    fun generateRecipe(recipeGenerationInfo: RecipeGenerationInfo) {
        runIfTokenIsValid {
            homebrewApiService.generateRecipe(authResult!!.authorizationHeader, recipeGenerationInfo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _generateRecipeResponse.postValue(it) },
                    { Log.d("", "generate recipe error", it) }
                )
                .disposeWhenCleared()
        }
    }

    fun updateRecipe(recipeId: String, recipeUpdateInfo: RecipeUpdateInfo) {
        runIfTokenIsValid {
            homebrewApiService.updateRecipe(authResult!!.authorizationHeader, recipeId, recipeUpdateInfo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                        _updateRecipeResponse.postValue(ApiResponse(true))
                    },
                    {
                        Log.d("", "update recipe error", it)
                        _updateRecipeResponse.postValue(ApiResponse(false))
                    }
                )
                .disposeWhenCleared()
        }
    }

    fun deleteRecipe(recipeId: String) {
        runIfTokenIsValid {
            homebrewApiService.deleteRecipe(authResult!!.authorizationHeader, recipeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _deleteRecipeResponse.postValue(ApiResponse(true))
                    },
                    {
                        Log.d("", "delete recipe error", it)
                        _deleteRecipeResponse.postValue(ApiResponse(false))
                    }
                )
                .disposeWhenCleared()
        }
    }

    fun loadRecipeDefaultSettings() {
        runIfTokenIsValid {
            homebrewApiService.getRecipeDefaultSettings(authResult!!.authorizationHeader)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _loadRecipeDefaultSettingsResponse.postValue(it) },
                    { Log.d("", "load recipe default settings error", it) }
                )
                .disposeWhenCleared()
        }
    }
}