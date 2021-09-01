package com.jlafshari.beerrecipegenerator.settings

import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.jlafshari.beerrecipegenerator.BuildConfig
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.MainActivity
import com.jlafshari.beerrecipegenerator.databinding.ActivitySettingsBinding
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper
import com.jlafshari.beerrecipegenerator.ui.login.LoginActivity
import java.util.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val txtUserName = binding.txtUserName
        txtUserName.text = AuthHelper.getUserName()

        val buildDate = Date(BuildConfig.TIMESTAMP)
        val txtBuildDate = binding.txtBuildDate
        txtBuildDate.text = buildDate.toString()

        val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        val version = pInfo.versionName
        val txtBuildVersion = binding.txtBuildVersion
        txtBuildVersion.text = version

        val txtExtractionEfficiency = binding.txtExtractionEfficiency
        setUpExtractionEfficiency(txtExtractionEfficiency)
    }

    private fun setUpExtractionEfficiency(txtExtractionEfficiency: EditText) {
        txtExtractionEfficiency.text.clear()
        txtExtractionEfficiency.text.insert(
            0,
            AppSettings.recipeDefaultSettings.extractionEfficiency.toString()
        )
        txtExtractionEfficiency.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(extractionEfficiencyEditText: Editable?) {
                val extractionEfficiency = extractionEfficiencyEditText.toString().toIntOrNull()
                if (extractionEfficiency != null)
                    AppSettings.updateExtractionEfficiency(
                        extractionEfficiency,
                        getSharedPreferences(AppSettings.PREFERENCE_FILE_NAME, MODE_PRIVATE)
                    )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onBackPressed() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun signOut(view: View) {
        val loginActivityIntent = Intent(this, LoginActivity::class.java)
        loginActivityIntent.putExtra(Constants.EXTRA_SIGN_OUT, true)
        startActivity(loginActivityIntent)
    }
}