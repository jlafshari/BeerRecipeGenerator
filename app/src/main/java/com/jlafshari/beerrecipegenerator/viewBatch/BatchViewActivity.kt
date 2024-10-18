package com.jlafshari.beerrecipegenerator.viewBatch

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jlafshari.beerrecipecore.batches.Batch
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.batches.BatchViewModel
import com.jlafshari.beerrecipegenerator.databinding.ActivityBatchViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BatchViewActivity : AppCompatActivity() {
    private lateinit var mBatch: Batch

    private val batchViewModel: BatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBatchViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val batchId = intent.getStringExtra(Constants.EXTRA_VIEW_BATCH)!!
        batchViewModel.loadBatchDetails(batchId)

        batchViewModel.loadBatchDetailsResponse.observe(this@BatchViewActivity) {
            if (it != null) {
                mBatch = it
                loadBatchView(binding)
                batchViewModel.loadBatchDetailsComplete()
            }
        }
    }

    private fun loadBatchView(binding: ActivityBatchViewBinding) {
        val txtBrewingDate = binding.root.findViewById<TextView>(R.id.txtBrewingDate)
        txtBrewingDate.text = mBatch.brewingDate
    }
}