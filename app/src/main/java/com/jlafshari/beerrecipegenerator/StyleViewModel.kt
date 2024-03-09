package com.jlafshari.beerrecipegenerator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlafshari.beerrecipecore.Style
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class StyleViewModel @Inject constructor(private val homebrewApiService: HomebrewApiService) :
    BaseViewModel() {

    private val _loadAllStylesResponse = MutableLiveData<List<Style>>()
    val loadAllStylesResponse: LiveData<List<Style>> = _loadAllStylesResponse

    fun loadAllStyles() {
        runIfTokenIsValid {
            homebrewApiService.getAllStyles(authResult!!.authorizationHeader)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _loadAllStylesResponse.postValue(it) },
                    { Log.d("", "get all styles error", it) }
                )
                .disposeWhenCleared()
        }
    }
}