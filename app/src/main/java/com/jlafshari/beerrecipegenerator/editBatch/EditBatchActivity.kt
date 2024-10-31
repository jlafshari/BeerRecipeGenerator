package com.jlafshari.beerrecipegenerator.editBatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.jlafshari.beerrecipegenerator.databinding.ActivityEditBatchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditBatchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditBatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtRecipeName.text = "Test Recipe"
    }
}
