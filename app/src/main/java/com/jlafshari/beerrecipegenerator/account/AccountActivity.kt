package com.jlafshari.beerrecipegenerator.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.MainActivity
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.databinding.ActivityAccountBinding
import com.jlafshari.beerrecipegenerator.ui.login.AzureAuthHelper
import com.jlafshari.beerrecipegenerator.ui.login.AzureLoginActivity

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val txtUserName = binding.txtUserName
        txtUserName.text = AzureAuthHelper.getUserName()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val mainActivityIntent = Intent(this@AccountActivity, MainActivity::class.java)
                startActivity(mainActivityIntent)
            }
        })
    }

    @Suppress("UNUSED_PARAMETER")
    fun signOut(view: View) {
        val loginActivityIntent = Intent(this, AzureLoginActivity::class.java)
        loginActivityIntent.putExtra(Constants.EXTRA_SIGN_OUT, true)
        startActivity(loginActivityIntent)
    }
}