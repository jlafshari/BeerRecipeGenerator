package com.jlafshari.beerrecipegenerator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlafshari.beerrecipecore.Fermentable
import com.jlafshari.beerrecipecore.Hop
import com.jlafshari.beerrecipegenerator.homebrewApi.HomebrewApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _loadHopDetailsResponse.postValue(it) },
                    { Log.d("", "get hop details error", it) }
                )
                .disposeWhenCleared()
        }
    }

    fun loadFermentableDetails(fermentableId: String) {
        runIfTokenIsValid {
            homebrewApiService.getFermentableDetails(authResult!!.authorizationHeader, fermentableId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _loadFermentableDetailsResponse.postValue(it) },
                    { Log.d("", "get fermentable details error", it) }
                )
                .disposeWhenCleared()
        }
    }

    fun loadAllFermentables() {
        runIfTokenIsValid {
            homebrewApiService.getAllFermentables(authResult!!.authorizationHeader)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _loadAllFermentablesResponse.postValue(it) },
                    { Log.d("", "get all fermentables error", it) }
                )
                .disposeWhenCleared()
        }
    }

    fun loadAllHops() {
        runIfTokenIsValid {
            homebrewApiService.getAllHops(authResult!!.authorizationHeader)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _loadAllHopsResponse.postValue(it) },
                    { Log.d("", "get all hops error", it) }
                )
                .disposeWhenCleared()
        }
    }
}