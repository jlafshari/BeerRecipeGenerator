package com.jlafshari.beerrecipegenerator.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.R
import com.okta.oidc.*
import com.okta.oidc.clients.web.WebAuthClient
import com.okta.oidc.storage.SharedPreferenceStorage
import com.okta.oidc.util.AuthorizationException
import java.util.*


object AuthHelper {
    private lateinit var webAuthClient: WebAuthClient

    fun getAccessToken() : String? {
        if (!webAuthClient.sessionClient.isAuthenticated) {
            refreshTokens()
        }

        return webAuthClient.sessionClient?.tokens?.accessToken
    }

    fun getUserName() : String? {
        val idToken = webAuthClient.sessionClient?.tokens?.idToken
        return if (idToken != null) {
            val payload = decodeJWTTokenPayload(idToken)
            val user: User = jacksonObjectMapper().readValue(payload)
            user.name
        } else {
            null
        }
    }

    private fun decodeJWTTokenPayload(token: String): String {
        val decoder: Base64.Decoder = Base64.getDecoder()
        val chunks = token.split(".").toTypedArray()

        return String(decoder.decode(chunks[1]))
    }

    fun startLoginActivity(context: Context) {
        val loginActivityIntent = Intent(context, LoginActivity::class.java)
        loginActivityIntent.putExtra(Constants.EXTRA_SIGN_OUT, true)
        context.startActivity(loginActivityIntent)
    }

    fun createWebClient(context: Context, resultCallback: ResultCallback<AuthorizationStatus, AuthorizationException>) {
        val config = OIDCConfig.Builder()
            .withJsonFile(context, R.raw.okta_config)
            .create()

        webAuthClient = Okta.WebAuthBuilder()
            .withConfig(config)
            .withContext(context)
            .withStorage(SharedPreferenceStorage(context, Constants.PREF_STORAGE_WEB))
            .setRequireHardwareBackedKeyStore(false)
            .create()

        webAuthClient.registerCallback(resultCallback, context as Activity?)
    }

    fun signIn(userName: String, activity: Activity) {
        webAuthClient.signIn(activity, AuthenticationPayload.Builder().setLoginHint(userName).build())
    }

    fun signOut(activity: Activity) {
        webAuthClient.signOutOfOkta(activity)
    }

    fun clearTokens() {
        webAuthClient.sessionClient?.clear()
    }

    private fun refreshTokens() {
        webAuthClient.sessionClient.refreshToken(object :
            RequestCallback<Tokens, AuthorizationException?> {
            override fun onSuccess(result: Tokens) {
                println("refreshed token: $result")
            }

            override fun onError(error: String?, exception: AuthorizationException?) {
                println("error refreshing token: $error $exception")
            }
        })
    }
}