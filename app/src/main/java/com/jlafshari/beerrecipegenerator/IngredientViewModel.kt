package com.jlafshari.beerrecipegenerator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlafshari.beerrecipecore.Hop
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class IngredientViewModel @Inject constructor(private val homebrewApiService: HomebrewApiService) :
    BaseViewModel() {

    private val _loadHopDetailsResponse = MutableLiveData<Hop>()
    val loadHopDetailsResponse: LiveData<Hop> = _loadHopDetailsResponse

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
}