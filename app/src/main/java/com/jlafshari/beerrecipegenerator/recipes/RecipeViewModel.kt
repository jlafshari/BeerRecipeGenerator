package com.jlafshari.beerrecipegenerator.recipes

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipegenerator.BaseViewModel
import com.jlafshari.beerrecipegenerator.HomebrewApiService
import com.jlafshari.beerrecipegenerator.ui.login.AzureAuthHelper
import com.microsoft.identity.client.IAuthenticationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val homebrewApiService: HomebrewApiService) : BaseViewModel() {

    private val _loadRecipePreviewsResponse = MutableLiveData<List<RecipePreview>>()
    val loadRecipePreviewsResponse: LiveData<List<RecipePreview>> = _loadRecipePreviewsResponse

    private var _authResult: IAuthenticationResult? = null

    @SuppressLint("CheckResult")
    fun loadRecipePreviews(abvMin: String?, abvMax: String?,
                           colorMin: String?, colorMax: String?,
                           yeastType: String?) {
        if (_authResult.isTokenInvalid()) {
            AzureAuthHelper.getAccessTokenAsync {
                _authResult = it
                loadRecipePreviewsWithAuth(abvMin, abvMax, colorMin, colorMax, yeastType)
            }
        }
        else {
            loadRecipePreviewsWithAuth(abvMin, abvMax, colorMin, colorMax, yeastType)
        }
    }

    private fun loadRecipePreviewsWithAuth(abvMin: String?, abvMax: String?,
        colorMin: String?, colorMax: String?,
        yeastType: String?
    ) {
        homebrewApiService.getAllRecipePreviews(_authResult?.accessToken,
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

    private fun IAuthenticationResult?.isTokenInvalid() : Boolean =
        this == null || this.expiresOn.before(Date())
}