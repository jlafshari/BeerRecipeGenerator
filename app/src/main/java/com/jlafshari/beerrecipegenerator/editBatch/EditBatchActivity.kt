package com.jlafshari.beerrecipegenerator.editBatch

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jlafshari.beerrecipecore.batches.Batch
import com.jlafshari.beerrecipecore.batches.BatchStatus
import com.jlafshari.beerrecipecore.batches.batchStatusEnumDisplayMap
import com.jlafshari.beerrecipecore.batches.displayText
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.batches.BatchViewModel
import com.jlafshari.beerrecipegenerator.databinding.ActivityEditBatchBinding
import com.jlafshari.beerrecipegenerator.viewBatch.BatchViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditBatchActivity : AppCompatActivity() {

    private lateinit var mBatch: Batch

    private lateinit var binding: ActivityEditBatchBinding
    private val batchViewModel: BatchViewModel by viewModels()
    private val batchStatusDisplayItems = BatchStatus.entries.map { it.displayText() }.toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val batchId = intent.getStringExtra(Constants.EXTRA_EDIT_BATCH)!!
        batchViewModel.loadBatchDetails(batchId)

        initializeStatusSpinner()

        batchViewModel.loadBatchDetailsResponse.observe(this@EditBatchActivity) {
            if (it != null) {
                loadBatch(it)
                batchViewModel.loadBatchDetailsComplete()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_batch, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_cancel_edit -> {
            cancelEditBatch()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun cancelEditBatch() = goBackToBatchView()

    private fun goBackToBatchView() {
        val batchViewIntent = Intent(this, BatchViewActivity::class.java)
        batchViewIntent.putExtra(Constants.EXTRA_VIEW_BATCH, mBatch.id)
        startActivity(batchViewIntent)
    }

    private fun loadBatch(batch: Batch) {
        with(batch) {
            mBatch = this
        }

        loadBatchView()
    }

    private fun loadBatchView() {
        binding.txtRecipeName.text = mBatch.recipe.name
        if (mBatch.notes != null) {
            binding.txtBatchNotes.text.insert(0, mBatch.notes!!)
        }

        val currentStatus = mBatch.statusHistory.last().status
        val currentStatusIndex = batchStatusDisplayItems.indexOf(currentStatus.displayText())
        binding.statusSpinner.setSelection(currentStatusIndex)
    }

    private fun initializeStatusSpinner() {
        binding.statusSpinner.adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            batchStatusDisplayItems
        )
        binding.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedDisplayText = parent?.getItemAtPosition(position).toString()
                val selectedEnum =
                    batchStatusEnumDisplayMap.entries.firstOrNull { it.value == selectedDisplayText }?.key
                selectedEnum?.let {
                    //TODO: save status selection
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}
