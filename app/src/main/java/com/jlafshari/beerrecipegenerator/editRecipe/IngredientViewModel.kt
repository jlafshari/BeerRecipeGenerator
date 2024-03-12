package com.jlafshari.beerrecipegenerator.editRecipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlafshari.beerrecipecore.Fermentable
import com.jlafshari.beerrecipecore.Hop
import com.jlafshari.beerrecipegenerator.BaseViewModel
import com.jlafshari.beerrecipegenerator.homebrewApi.HomebrewApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IngredientViewModel @Inject constructor(private val homebrewApiService: HomebrewApiService) :
    BaseViewModel() {

    private val _loadHopDetailsResponse = MutableLiveData<Hop>()
    val loadHopDetailsResponse: LiveData<Hop> = _loadHopDetailsResponse

    private val _loadFermentableDetailsResponse = MutableLiveData<Fermentable>()
    val loadFermentableDetailsResponse: LiveData<Fermentable> = _loadFermentableDetailsResponse

    private val _loadAllFermentablesResponse = MutableLiveData<List<Fermentable>>()
    val loadAllFermentablesResponse: LiveData<List<Fermentable>> = _loadAllFermentablesResponse

    private val _loadAllHopsResponse = MutableLiveData<List<Hop>>()
    val loadAllHopsResponse: LiveData<List<Hop>> = _loadAllHopsResponse

    fun loadHopDetails(hopId: String) {
        runIfTokenIsValid {
            homebrewApiService.getHopDetails(authResult!!.authorizationHeader, hopId)
                .subscribeThenDispose(
                    { _loadHopDetailsResponse.postValue(it) },
                    { Log.d("", "get hop details error", it) }
                )
        }
    }

    fun loadFermentableDetails(fermentableId: String) {
        runIfTokenIsValid {
            homebrewApiService.getFermentableDetails(authResult!!.authorizationHeader, fermentableId)
                .subscribeThenDispose(
                    { _loadFermentableDetailsResponse.postValue(it) },
                    { Log.d("", "get fermentable details error", it) }
                )
        }
    }

    fun loadAllFermentables() {
        runIfTokenIsValid {
            homebrewApiService.getAllFermentables(authResult!!.authorizationHeader)
                .subscribeThenDispose(
                    { _loadAllFermentablesResponse.postValue(it) },
                    { Log.d("", "get all fermentables error", it) }
                )
        }
    }

    fun loadAllHops() {
        runIfTokenIsValid {
            homebrewApiService.getAllHops(authResult!!.authorizationHeader)
                .subscribeThenDispose(
                    { _loadAllHopsResponse.postValue(it) },
                    { Log.d("", "get all hops error", it) }
                )
        }
    }
}