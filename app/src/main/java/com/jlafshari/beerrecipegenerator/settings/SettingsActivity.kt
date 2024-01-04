package com.jlafshari.beerrecipegenerator.settings

import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.jlafshari.beerrecipegenerator.BuildConfig
import com.jlafshari.beerrecipegenerator.MainActivity
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.databinding.ActivitySettingsBinding
import java.util.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val buildDate = Date(BuildConfig.TIMESTAMP)
        val txtBuildDate = binding.txtBuildDate
        txtBuildDate.text = buildDate.toString()

        val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        val version = pInfo.versionName
        val txtBuildVersion = binding.txtBuildVersion
        txtBuildVersion.text = version

        val txtExtractionEfficiency = binding.txtExtractionEfficiency
        setUpExtractionEfficiency(txtExtractionEfficiency, binding.txtSettingsError)

        val txtMashThickness = binding.txtMashThickness
        setUpMashThickness(txtMashThickness)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val mainActivityIntent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(mainActivityIntent)
            }
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
                        AppSettings.updateExtractionEfficiency(
                            extractionEfficiency,
                            getSharedPreferences(AppSettings.PREFERENCE_FILE_NAME, MODE_PRIVATE)
                        )
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

    private fun setUpMashThickness(txtMashThickness: TextView) {
        txtMashThickness.text = AppSettings.recipeDefaultSettings.mashThickness.toString()
    }
}