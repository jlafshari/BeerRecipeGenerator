package com.jlafshari.beerrecipegenerator.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.jlafshari.beerrecipegenerator.MainActivity
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        with(binding) {
            setUpExtractionEfficiency(txtExtractionEfficiency, txtSettingsError)
            setUpMashThickness(txtMashThickness)
            setUpRecipeSize(txtRecipeSize)
            setUpBoilDuration(txtBoilDuration)
        }

        sharedPreferences = getSharedPreferences(AppSettings.PREFERENCE_FILE_NAME, MODE_PRIVATE)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val mainActivityIntent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(mainActivityIntent)
            }
        })
    }

    private fun setUpBoilDuration(txtBoilDuration: EditText) {
        txtBoilDuration.text.clear()
        val defaultBoilDuration = AppSettings.recipeDefaultSettings.boilDurationMinutes.toString()
        txtBoilDuration.text.insert(0, defaultBoilDuration)
        txtBoilDuration.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(boilDurationEditText: Editable?) {
                val boilDuration = boilDurationEditText.toString().toIntOrNull()
                if (boilDuration != null) {
                    AppSettings.updateBoilDuration(boilDuration, sharedPreferences!!)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setUpRecipeSize(txtRecipeSize: EditText) {
        txtRecipeSize.text.clear()
        val defaultRecipeSize = "%.1f".format(AppSettings.recipeDefaultSettings.recipeSize)
        txtRecipeSize.text.insert(0, defaultRecipeSize)
        txtRecipeSize.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(recipeSizeEditText: Editable?) {
                val recipeSize = recipeSizeEditText.toString().toDoubleOrNull()
                if (recipeSize != null) {
                    AppSettings.updateRecipeSize(recipeSize, sharedPreferences!!)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setUpExtractionEfficiency(
        txtExtractionEfficiency: EditText,
        txtSettingsError: TextView
    ) {
        txtExtractionEfficiency.text.clear()
        txtExtractionEfficiency.text.insert(
            0,
            AppSettings.recipeDefaultSettings.extractionEfficiency.toString()
        )
        txtExtractionEfficiency.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(extractionEfficiencyEditText: Editable?) {
                val extractionEfficiency = extractionEfficiencyEditText.toString().toIntOrNull()
                if (extractionEfficiency != null) {
                    if (extractionEfficiency in 0..100) {
                        AppSettings.updateExtractionEfficiency(extractionEfficiency, sharedPreferences!!)
                        txtSettingsError.text = ""
                    }
                    else {
                        txtSettingsError.text = getString(R.string.extraction_efficiency_error)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setUpMashThickness(txtMashThickness: EditText) {
        txtMashThickness.text.clear()
        val defaultMashThickness = "%.2f".format(AppSettings.recipeDefaultSettings.mashThickness)
        txtMashThickness.text.insert(0, defaultMashThickness)
        txtMashThickness.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(mashThicknessEditText: Editable?) {
                val mashThickness = mashThicknessEditText.toString().toDoubleOrNull()
                if (mashThickness != null) {
                    AppSettings.updateMashThickness(mashThickness, sharedPreferences!!)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}