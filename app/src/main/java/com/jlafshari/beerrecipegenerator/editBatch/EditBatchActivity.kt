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
import com.jlafshari.beerrecipecore.batches.BatchUpdateInfo
import com.jlafshari.beerrecipecore.batches.GravityReading
import com.jlafshari.beerrecipecore.batches.batchStatusEnumDisplayMap
import com.jlafshari.beerrecipecore.batches.displayText
import com.jlafshari.beerrecipecore.utility.DateUtility
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.batches.BatchViewModel
import com.jlafshari.beerrecipegenerator.databinding.ActivityEditBatchBinding
import com.jlafshari.beerrecipegenerator.viewBatch.BatchViewActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.util.Locale

@AndroidEntryPoint
class EditBatchActivity : AppCompatActivity() {

    private lateinit var mBatchId: String
    private lateinit var mBatch: Batch
    private lateinit var mBatchUpdateInfo: BatchUpdateInfo
    private var mGravityReadingValue: Double = 0.0

    private lateinit var binding: ActivityEditBatchBinding
    private val batchViewModel: BatchViewModel by viewModels()
    private val batchStatusDisplayItems = BatchStatus.entries.map { it.displayText() }.toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mBatchId = intent.getStringExtra(Constants.EXTRA_EDIT_BATCH)!!
        batchViewModel.loadBatchDetails(mBatchId)

        binding.btnUpdate.setOnClickListener { updateBatch() }

        binding.chkRefractometerReading.setOnCheckedChangeListener { _, _ ->
            getGravityReading()
        }

        batchViewModel.loadBatchDetailsResponse.observe(this@EditBatchActivity) {
            if (it != null) {
                loadBatch(it)
                batchViewModel.loadBatchDetailsComplete()
            }
        }

        batchViewModel.updateBatchResponse.observe(this@EditBatchActivity) {
            if (it.isSuccessful) {
                goBackToBatchView()
            }
        }

        batchViewModel.getCorrectedRefractometerReadingResponse.observe(this@EditBatchActivity) {
            if (it != null) {
                mGravityReadingValue = it
                batchViewModel.getCorrectedRefractometerReadingComplete()
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

    private fun updateBatch() {
        mBatchUpdateInfo.notes = binding.txtBatchNotes.text.toString()
        mBatchUpdateInfo.assistantBrewerName = binding.txtAssistantBrewer.text.toString()

        if (mBatchUpdateInfo.status == mBatch.currentStatus()) {
            mBatchUpdateInfo.status = null
        }

        if (mGravityReadingValue > 1) {
            mBatchUpdateInfo.gravityReadings.add(
                GravityReading(
                    mGravityReadingValue,
                    DateUtility.getFormattedTimeStamp(Instant.now())
                )
            )
        }

        batchViewModel.updateBatch(mBatchId, mBatchUpdateInfo)
    }

    private fun getGravityReading() {
        val gravityReadingText = binding.gravityReadingSpinner.selectedItem.toString()
        if (gravityReadingText.isEmpty()) return

        val gravityDoubleValue = gravityReadingText.toDoubleOrNull() ?: return

        if (binding.chkRefractometerReading.isChecked) {
            val originalGravity = mBatch.gravityReadings.firstOrNull()
            originalGravity?.let {
                batchViewModel.getCorrectedRefractometerReading(it.value, gravityDoubleValue)
            }
        } else {
            mGravityReadingValue = gravityDoubleValue
        }
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
            mBatchUpdateInfo = BatchUpdateInfo(
                mBatch.gravityReadings.toMutableList(),
                mBatch.currentStatus(),
                mBatch.assistantBrewerName,
                mBatch.notes
            )
        }

        loadBatchView()
    }

    private fun loadBatchView() {
        binding.txtRecipeName.text = mBatch.recipe.name
        if (mBatch.notes != null) {
            binding.txtBatchNotes.text.insert(0, mBatch.notes!!)
        }
        if (mBatch.assistantBrewerName != null) {
            binding.txtAssistantBrewer.text.insert(0, mBatch.assistantBrewerName!!)
        }

        initializeStatusSpinner()
        initializeGravityReadingSpinner()
        val currentStatus = mBatch.currentStatus()
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
                val selectedBatchStatusEnum =
                    batchStatusEnumDisplayMap.entries.firstOrNull { it.value == selectedDisplayText }?.key
                selectedBatchStatusEnum?.let {
                    mBatchUpdateInfo.status = it
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun initializeGravityReadingSpinner() {
        val recentBatchGravityReading = mBatch.gravityReadings.lastOrNull()?.value?.let {
            (it * 1000) - 1000
        }
        val maxGravityReadingValue = recentBatchGravityReading?.toInt() ?: 75
        val gravityReadingValues = mutableListOf("Select")
        for (n in maxGravityReadingValue downTo 0) {
            val formattedNumber = String.format(Locale.getDefault(), "%02d", n)
            gravityReadingValues.add("1.0$formattedNumber")
        }

        binding.gravityReadingSpinner.adapter = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item,
            gravityReadingValues
        )

        binding.gravityReadingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getGravityReading()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }
}
