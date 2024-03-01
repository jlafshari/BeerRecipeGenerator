package com.jlafshari.beerrecipegenerator.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.jlafshari.beerrecipegenerator.B2CConfig
import com.jlafshari.beerrecipegenerator.R
import com.microsoft.identity.client.AcquireTokenSilentParameters
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.SilentAuthenticationCallback
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.common.java.providers.oauth2.IDToken


object AzureAuthHelper {
    private var b2cApplication: ISingleAccountPublicClientApplication? = null
    var account : IAccount? = null

    fun signIn(activity: Activity, signInSuccessCallback: () -> Unit,
               signInErrorCallback: (exception: MsalException?) -> Unit) {
        b2cApplication?.signIn(
            activity,
            null,
            B2CConfig.scopes,
            object : AuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                    loadAccount()
                    signInSuccessCallback()
                }

                override fun onError(exception: MsalException?) {
                    signInErrorCallback(exception)
                }

                override fun onCancel() { }
            })
    }

    fun signOut() {
        b2cApplication?.signOut(object : ISingleAccountPublicClientApplication.SignOutCallback {
            override fun onSignOut() {
                account = null
            }

            override fun onError(exception: MsalException) {
            }
        })
    }

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

    fun getAccessTokenAsync(success: (authResult: IAuthenticationResult?) -> Unit) {
        if (account == null) {
            loadAccount()
        }

        val parameters = AcquireTokenSilentParameters.Builder()
            .fromAuthority(B2CConfig.authorityUrl)
            .withScopes(B2CConfig.scopes.toList())
            .forAccount(account)
            .withCallback(object : SilentAuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                    success(authenticationResult)
                }

                override fun onError(exception: MsalException?) {
                }

            })
            .build()
        b2cApplication?.acquireTokenSilentAsync(parameters)
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

    fun isUserSignedIn(context: Context, isSignedInCallback: () -> Unit) {
        b2cApplication!!.getCurrentAccountAsync(object :
            ISingleAccountPublicClientApplication.CurrentAccountCallback {
            override fun onAccountLoaded(activeAccount: IAccount?) {
                if (activeAccount != null) {
                    isSignedInCallback()
                } else {
                    showLoginScreen(context)
                }
            }

            override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) { }

            override fun onError(exception: MsalException) {
                showLoginScreen(context)
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

    fun showLoginScreen(context: Context) {
        val loginActivityIntent = Intent(context, AzureLoginActivity::class.java)
        context.startActivity(loginActivityIntent)
    }
}