package com.jlafshari.beerrecipegenerator.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jlafshari.beerrecipegenerator.MainActivity
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.databinding.ActivityLoginBinding
import com.jlafshari.beerrecipegenerator.newRecipe.NewRecipeWizardActivity
import com.okta.oidc.*
import com.okta.oidc.clients.sessions.SessionClient
import com.okta.oidc.clients.web.WebAuthClient
import com.okta.oidc.storage.SharedPreferenceStorage
import com.okta.oidc.util.AuthorizationException
import kotlin.properties.Delegates

const val PREF_STORAGE_WEB: String = "web_client"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val tag = LoginActivity::class.simpleName

    private var config: OIDCConfig by Delegates.notNull()
    private lateinit var webAuthClient: WebAuthClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val login = binding.login

        config = OIDCConfig.Builder()
            .withJsonFile(this, R.raw.okta_config)
            .create()

        createWebClient()
    }

    private fun createWebClient() {
        webAuthClient = Okta.WebAuthBuilder()
            .withConfig(config)
            .withContext(this)
            .withStorage(SharedPreferenceStorage(this, PREF_STORAGE_WEB))
            .setRequireHardwareBackedKeyStore(false)
            .create()

        webAuthClient.registerCallback(object :
            ResultCallback<AuthorizationStatus, AuthorizationException> {
            override fun onCancel() {
//                network_progress.hide()
                showMessage(getString(R.string.operation_cancelled))
            }

            override fun onError(msg: String?, exception: AuthorizationException?) {
                signInError(msg, exception)
            }

            override fun onSuccess(result: AuthorizationStatus) {
//                network_progress.hide()
                when (result) {
                    AuthorizationStatus.AUTHORIZED -> signInSuccess()
                    AuthorizationStatus.SIGNED_OUT -> showMessage(getString(R.string.sign_out_success))
                }
            }
        }, this)
    }

    private fun showMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
            .let { bar ->
                bar.setAction(getString(R.string.dismiss)) {
                    bar.dismiss()
                }
            }.show()
    }

    private fun signInSuccess() {
        showMessage(getString(R.string.authorized))
        AuthHelper.sessionClient = webAuthClient.sessionClient
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }

    private fun signInError(msg: String?, exception: AuthorizationException?) {
//        network_progress.hide()
        showMessage(msg ?: getString(R.string.unknown))
        Log.d(tag, "onError: ", exception)
    }

    fun signIn(view: View) {
        val userName = binding.username.text.toString()
        webAuthClient.signIn(this, AuthenticationPayload.Builder().setLoginHint(userName).build())
    }

//    fun getSession(): SessionClient? {
//        return webAuthClient.sessionClient
//    }
}