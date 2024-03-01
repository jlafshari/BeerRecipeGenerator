package com.jlafshari.beerrecipegenerator.recipes

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipegenerator.HomebrewApiService
import com.jlafshari.beerrecipegenerator.ui.login.AzureAuthHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val homebrewApiService: HomebrewApiService) : ViewModel() {
    private val _loadRecipePreviewsResponse = MutableLiveData<List<RecipePreview>>()
    val loadRecipePreviewsResponse: LiveData<List<RecipePreview>> = _loadRecipePreviewsResponse
    private val _loadAccessTokenResponse = MutableLiveData<String?>()
    val loadAccessTokenResponse: LiveData<String?> = _loadAccessTokenResponse

    @SuppressLint("CheckResult")
    fun loadRecipePreviews() {
        homebrewApiService.getAllRecipePreviews(loadAccessTokenResponse.value)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _loadRecipePreviewsResponse.postValue(it) },
                { Log.d("", "load recipe previews error ", it) }
            )
    }

    fun loadAccessToken() {
        AzureAuthHelper.getAccessTokenAsync { _loadAccessTokenResponse.postValue(it) }
    }
}