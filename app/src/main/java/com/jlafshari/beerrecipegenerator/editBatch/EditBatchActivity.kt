package com.jlafshari.beerrecipegenerator.editBatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.jlafshari.beerrecipecore.batches.Batch
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.batches.BatchViewModel
import com.jlafshari.beerrecipegenerator.databinding.ActivityEditBatchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditBatchActivity : ComponentActivity() {

    private lateinit var mBatch: Batch

    private lateinit var binding: ActivityEditBatchBinding
    private val batchViewModel: BatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val batchId = intent.getStringExtra(Constants.EXTRA_EDIT_BATCH)!!
        batchViewModel.loadBatchDetails(batchId)

        batchViewModel.loadBatchDetailsResponse.observe(this@EditBatchActivity) {
            if (it != null) {
                loadBatch(it)
                batchViewModel.loadBatchDetailsComplete()
            }
        }
    }

    private fun loadBatch(batch: Batch) {
        with(batch) {
            mBatch = this
        }

        loadBatchView()
    }

    private fun loadBatchView() {
        binding.txtRecipeName.text = mBatch.recipe.name
    }
}
