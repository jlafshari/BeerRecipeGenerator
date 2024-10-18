package com.jlafshari.beerrecipegenerator.batches

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlafshari.beerrecipecore.batches.Batch
import com.jlafshari.beerrecipegenerator.BaseViewModel
import com.jlafshari.beerrecipegenerator.homebrewApi.HomebrewApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BatchViewModel @Inject constructor(private val homebrewApiService: HomebrewApiService) :
    BaseViewModel() {
    private val _loadBatchDetailsResponse = MutableLiveData<Batch?>()
    val loadBatchDetailsResponse: LiveData<Batch?> = _loadBatchDetailsResponse

    fun loadBatchDetails(batchId: String) {
        runIfTokenIsValid {
            homebrewApiService.getBatchDetails(authResult!!.authorizationHeader, batchId)
                .subscribeThenDispose(
                    { _loadBatchDetailsResponse.postValue(it) },
                    { Log.d("", "load batch details error", it) }
                )
        }
    }

    fun loadBatchDetailsComplete() {
        _loadBatchDetailsResponse.postValue(null)
    }
}