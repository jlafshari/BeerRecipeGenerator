package com.jlafshari.beerrecipegenerator.viewBatch

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.databinding.ActivityBatchViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BatchViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBatchViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val txtBrewingDate = binding.root.findViewById<TextView>(R.id.txtBrewingDate)
        txtBrewingDate.text = "Test brewing date"
    }
}