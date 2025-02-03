package com.jlafshari.beerrecipegenerator.batches

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlafshari.beerrecipecore.batches.Batch
import com.jlafshari.beerrecipecore.batches.BatchUpdateInfo
import com.jlafshari.beerrecipegenerator.BaseViewModel
import com.jlafshari.beerrecipegenerator.homebrewApi.ApiResponse
import com.jlafshari.beerrecipegenerator.homebrewApi.HomebrewApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BatchViewModel @Inject constructor(private val homebrewApiService: HomebrewApiService) :
    BaseViewModel() {
    private val _loadBatchDetailsResponse = MutableLiveData<Batch?>()
    val loadBatchDetailsResponse: LiveData<Batch?> = _loadBatchDetailsResponse

    private val _updateBatchResponse = MutableLiveData<ApiResponse>()
    val updateBatchResponse: LiveData<ApiResponse> = _updateBatchResponse

    private val _getCorrectedRefractometerReadingResponse = MutableLiveData<Double?>()
    val getCorrectedRefractometerReadingResponse: LiveData<Double?> = _getCorrectedRefractometerReadingResponse

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

    fun updateBatch(batchId: String, batchUpdateInfo: BatchUpdateInfo) {
        runIfTokenIsValid {
            homebrewApiService.updateBatch(authResult!!.authorizationHeader, batchId, batchUpdateInfo)
                .subscribeThenDispose({
                    _updateBatchResponse.postValue(ApiResponse(true))
                },
                {
                    Log.d("", "update batch error", it)
                    _updateBatchResponse.postValue(ApiResponse(false))
                })
        }
    }

    fun getCorrectedRefractometerReading(originalGravity: Double, finalGravity: Double) {
        runIfTokenIsValid {
            homebrewApiService.getCorrectedRefractometerReading(authResult!!.authorizationHeader,
                originalGravity, finalGravity)
                .subscribeThenDispose({
                    _getCorrectedRefractometerReadingResponse.postValue(it)
                },
                {
                    Log.d("", "get corrected refractometer reading error", it)
                })
        }
    }

    fun getCorrectedRefractometerReadingComplete() {
        _getCorrectedRefractometerReadingResponse.postValue(null)
    }
}