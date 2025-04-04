package com.jlafshari.beerrecipegenerator.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.MainActivity
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.databinding.ActivityAzureLoginBinding
import com.jlafshari.beerrecipegenerator.login.AzureAuthHelper.getAccessTokenAsync
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.ISingleAccountPublicClientApplication.CurrentAccountCallback
import com.microsoft.identity.client.exception.MsalException

class AzureLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAzureLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAzureLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener { signIn() }

        AzureAuthHelper.initializeB2CApp(this, currentAccountCallback())

        val signOutValue = intent.getBooleanExtra(Constants.EXTRA_SIGN_OUT, false)
        if (signOutValue)
            signOut()
    }

    private fun currentAccountCallback() : CurrentAccountCallback {
        return object : CurrentAccountCallback {
            override fun onAccountLoaded(activeAccount: IAccount?) {
                if (activeAccount != null) {
                    AzureAuthHelper.loadAccount(activeAccount)
                    getAccessTokenAsync({
                        signInSuccess()
                    },
                    { signOut() })
                }
            }

            override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {
            }

            override fun onError(exception: MsalException) {
            }
        }
    }

    private fun signIn() {
        AzureAuthHelper.signIn(this, { signInSuccess() }, { exception ->
            showMessage(exception?.message!!)
            print(exception) })
    }

    private fun showMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun signInSuccess() {
        showMessage(getString(R.string.authorized))

        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)

        val loginButton = binding.login
        loginButton.isEnabled = true
    }

    private fun signOut() {
        AzureAuthHelper.signOut()
    }
}