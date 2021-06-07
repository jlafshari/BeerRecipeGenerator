package com.jlafshari.beerrecipegenerator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jlafshari.beerrecipegenerator.databinding.ActivitySettingsBinding
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper

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
    }

    override fun onBackPressed() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
    }
}