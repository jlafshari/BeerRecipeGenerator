package com.jlafshari.beerrecipegenerator.newRecipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlafshari.beerrecipecore.Style
import com.jlafshari.beerrecipegenerator.BaseViewModel
import com.jlafshari.beerrecipegenerator.homebrewApi.HomebrewApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StyleViewModel @Inject constructor(private val homebrewApiService: HomebrewApiService) :
    BaseViewModel() {

    private val _loadAllStylesResponse = MutableLiveData<List<Style>>()
    val loadAllStylesResponse: LiveData<List<Style>> = _loadAllStylesResponse

    fun loadAllStyles() {
        runIfTokenIsValid {
            homebrewApiService.getAllStyles(authResult!!.authorizationHeader)
                .subscribeThenDispose(
                    { _loadAllStylesResponse.postValue(it) },
                    { Log.d("", "get all styles error", it) }
                )
        }
    }
}