package com.jlafshari.beerrecipegenerator.ui.login

import android.content.Context
import android.util.Log
import com.jlafshari.beerrecipegenerator.R
import com.microsoft.identity.client.AcquireTokenSilentParameters
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.exception.MsalException

object AzureAuthHelper {
    var accessToken: String? = null
    private var b2cApplication: ISingleAccountPublicClientApplication? = null

//    fun getAccessToken() : String? {
//        return null
//        return b2cApplication.acquireTokenSilent(AcquireTokenSilentParameters(,))
//    }

    private fun initializeB2CApp(context: Context) {
        if (b2cApplication != null) { return }

        PublicClientApplication.createSingleAccountPublicClientApplication(
            context,
            R.raw.auth_config,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    b2cApplication = application
                }

                override fun onError(exception: MsalException) {
                    Log.d("", "onError: ", exception)
                }
            })
    }
}