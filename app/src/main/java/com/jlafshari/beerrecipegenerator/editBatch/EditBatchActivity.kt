package com.jlafshari.beerrecipegenerator.editBatch

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        binding.txtGravityReading.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getGravityReading()
            }

            override fun afterTextChanged(s: Editable?) { }
        })
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
        val gravityReadingText = binding.txtGravityReading.text.toString()
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
}
