package com.jlafshari.beerrecipegenerator.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.databinding.ActivityAccountBinding
import com.jlafshari.beerrecipegenerator.login.AzureAuthHelper
import com.jlafshari.beerrecipegenerator.login.AzureLoginActivity

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val signOutButton = binding.btnSignOut
        signOutButton.setOnClickListener { signOut() }

        AzureAuthHelper.isUserSignedIn(this) {
            val txtUserName = binding.txtUserName
            txtUserName.text = AzureAuthHelper.getUserName()
        }
    }

    private fun signOut() {
        val loginActivityIntent = Intent(this, AzureLoginActivity::class.java)
        loginActivityIntent.putExtra(Constants.EXTRA_SIGN_OUT, true)
        startActivity(loginActivityIntent)
    }
}