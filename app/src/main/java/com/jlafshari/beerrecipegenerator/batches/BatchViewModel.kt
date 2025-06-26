package com.jlafshari.beerrecipegenerator.batches

import android.util.Log
import com.jlafshari.beerrecipecore.batches.BatchPreview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlafshari.beerrecipecore.batches.Batch
import com.jlafshari.beerrecipecore.batches.BatchUpdateInfo
import com.jlafshari.beerrecipecore.batches.NewBatchInfo
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

    private val _newBatchResponse = MutableLiveData<String?>()
    val newBatchResponse: LiveData<String?> = _newBatchResponse

    private val _batchesInProgressResponse = MutableLiveData<List<BatchPreview>>()
    val batchesInProgressResponse: LiveData<List<BatchPreview>> = _batchesInProgressResponse

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

    fun loadBatchesInProgress() {
        runIfTokenIsValid {
            homebrewApiService.getBatchesInProgress(authResult!!.authorizationHeader)
                .subscribeThenDispose(
                    { _batchesInProgressResponse.postValue(it) },
                    { Log.d("", "load batches in progress error", it) }
                )
        }
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

    fun newBatch(newBatchInfo: NewBatchInfo) {
        runIfTokenIsValid {
            homebrewApiService.newBatch(authResult!!.authorizationHeader, newBatchInfo)
                .subscribeThenDispose({
                    _newBatchResponse.postValue(it)
                }, {
                    Log.d("", "new batch error", it)
                })
        }
    }

    fun newBatchComplete() {
        _newBatchResponse.postValue(null)
    }
}