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
import com.jlafshari.beerrecipegenerator.databinding.ActivityLoginBinding
import com.okta.oidc.AuthorizationStatus
import com.okta.oidc.ResultCallback
import com.okta.oidc.util.AuthorizationException

const val PREF_STORAGE_WEB: String = "web_client"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val tag = LoginActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AuthHelper.createWebClient(this, object :
            ResultCallback<AuthorizationStatus, AuthorizationException> {
            override fun onCancel() {
                showMessage(getString(R.string.operation_cancelled))
            }

            override fun onError(msg: String?, exception: AuthorizationException?) {
                signInError(msg, exception)
            }

            override fun onSuccess(result: AuthorizationStatus) {
                when (result) {
                    AuthorizationStatus.AUTHORIZED -> signInSuccess()
                    AuthorizationStatus.SIGNED_OUT -> {
                        AuthHelper.clearTokens()
                        showMessage(getString(R.string.sign_out_success))
                    }
                }
            }
        })

        val signOutValue = intent.getBooleanExtra(Constants.EXTRA_SIGN_OUT, false)
        if (signOutValue)
            signOut()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun signInSuccess() {
        showMessage(getString(R.string.authorized))
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }

    private fun signInError(msg: String?, exception: AuthorizationException?) {
        showMessage(msg ?: getString(R.string.unknown))
        Log.d(tag, "onError: ", exception)
    }

    fun signIn(view: View) {
        val userName = binding.username.text.toString()
        AuthHelper.signIn(userName, this)
    }

    private fun signOut() {
        AuthHelper.signOut(this)
    }
}