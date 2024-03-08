package com.jlafshari.beerrecipegenerator

import androidx.lifecycle.ViewModel
import com.jlafshari.beerrecipegenerator.ui.login.AzureAuthHelper
import com.microsoft.identity.client.IAuthenticationResult
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.Date

abstract class BaseViewModel : ViewModel() {
    protected var authResult: IAuthenticationResult? = null
    private val compositeDisposable = CompositeDisposable()

    internal fun Disposable.disposeWhenCleared(): Disposable {
        compositeDisposable.add(this)
        return this
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    protected fun runIfTokenIsValid(fn: () -> Unit) {
        if (authResult.isTokenInvalid()) {
            AzureAuthHelper.getAccessTokenAsync {
                authResult = it
                fn()
            }
        } else {
            fn()
        }
    }

    private fun IAuthenticationResult?.isTokenInvalid() : Boolean =
        this == null || this.expiresOn.before(Date())
}