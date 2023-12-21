package com.jlafshari.beerrecipegenerator.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.MainActivity
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.databinding.ActivityAzureLoginBinding
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalException

class AzureLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAzureLoginBinding
    private var msalApplication: ISingleAccountPublicClientApplication? = null
    private val tag = AzureLoginActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAzureLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PublicClientApplication.createSingleAccountPublicClientApplication(
            this,
            R.raw.auth_config,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    /**
                     * This test app assumes that the app is only going to support one account.
                     * This requires "account_mode" : "SINGLE" in the config json file.
                     *
                     */
                    msalApplication = application
                }

                override fun onError(exception: MsalException) {
                    showMessage(exception.message!!)
                    Log.d(tag, "onError: ", exception)
                }
            })

        val signOutValue = intent.getBooleanExtra(Constants.EXTRA_SIGN_OUT, false)
        if (signOutValue)
            signOut()
    }

    fun signIn(view: View) {
        val loginButton = binding.login
        //loginButton.isEnabled = false

        val scopes = arrayOf("https://homebrewingapp.onmicrosoft.com/homebrew-api/homebrew.recipeCreator")
        msalApplication!!.signIn(
            this,
            null,
            scopes,
            object : AuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                    AzureAuthHelper.accessToken = authenticationResult?.accessToken
                    signInSuccess()
                }

                override fun onError(exception: MsalException?) {
                    print(exception!!)
                }

                override fun onCancel() {
                    print("Cancelled")
                }
            })
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
        msalApplication!!.signOut()
    }
}