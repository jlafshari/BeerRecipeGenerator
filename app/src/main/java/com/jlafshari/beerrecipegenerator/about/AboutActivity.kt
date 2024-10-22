package com.jlafshari.beerrecipegenerator.about

import android.content.pm.PackageInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jlafshari.beerrecipegenerator.BuildConfig
import com.jlafshari.beerrecipegenerator.databinding.ActivityAboutBinding
import java.util.Date

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        loadAppInfo(binding)
    }

    private fun loadAppInfo(binding: ActivityAboutBinding) {
        val buildDate = Date(BuildConfig.TIMESTAMP)
        val txtBuildDate = binding.txtBuildDate
        txtBuildDate.text = buildDate.toString()

        val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        val version = pInfo.versionName
        val txtBuildVersion = binding.txtBuildVersion
        txtBuildVersion.text = version
    }
}