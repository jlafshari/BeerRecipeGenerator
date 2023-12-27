package com.jlafshari.beerrecipegenerator.ui.login

import android.content.Context
import android.util.Log
import com.jlafshari.beerrecipegenerator.B2CConfig
import com.jlafshari.beerrecipegenerator.R
import com.microsoft.identity.client.AcquireTokenSilentParameters
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.common.java.providers.oauth2.IDToken


object AzureAuthHelper {
    var b2cApplication: ISingleAccountPublicClientApplication? = null
    var account : IAccount? = null

    fun getAccessToken() : String? {
        if (account == null) {
            loadAccount()
        }

        val parameters = AcquireTokenSilentParameters.Builder()
            .fromAuthority(B2CConfig.authorityUrl)
            .withScopes(B2CConfig.scopes.toList())
            .forAccount(account)
            .build()
        return b2cApplication?.acquireTokenSilent(parameters)?.accessToken
    }

    fun getUserName() : String? {
        val displayName = account?.claims!![IDToken.NAME] ?: return null
        return displayName.toString()
    }

    fun loadAccount() {
        b2cApplication!!.getCurrentAccountAsync(object :
            ISingleAccountPublicClientApplication.CurrentAccountCallback {
            override fun onAccountLoaded(activeAccount: IAccount?) {
                account = activeAccount
            }

            override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {
            }

            override fun onError(exception: MsalException) {
            }
        })
    }

    fun initializeB2CApp(context: Context, callback: ISingleAccountPublicClientApplication.CurrentAccountCallback) {
        if (b2cApplication != null) { return }

        PublicClientApplication.createSingleAccountPublicClientApplication(
            context,
            R.raw.auth_config,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    b2cApplication = application
                    b2cApplication!!.getCurrentAccountAsync(callback)
                }

                override fun onError(exception: MsalException) {
                    Log.d("", "onError: ", exception)
                }
            })
    }
}