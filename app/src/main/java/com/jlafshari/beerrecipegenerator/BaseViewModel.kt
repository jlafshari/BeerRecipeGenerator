package com.jlafshari.beerrecipegenerator

import androidx.lifecycle.ViewModel
import com.jlafshari.beerrecipegenerator.login.AzureAuthHelper
import com.microsoft.identity.client.IAuthenticationResult
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException
import java.util.Date
import java.util.concurrent.TimeUnit

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
            AzureAuthHelper.getAccessTokenAsync(
                {
                    authResult = it
                    fn()
                },
                { }
            )
        } else {
            fn()
        }
    }

    protected fun <T> Single<T>.subscribeThenDispose(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
        this
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess(it) }, { onError(it) })
            .disposeWhenCleared()
    }

    protected fun <T> Observable<T>.subscribeThenDispose(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
        this
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess(it) }, { onError(it) })
            .disposeWhenCleared()
    }

    protected fun Completable.subscribeThenDispose(onComplete: Action, onError: (Throwable) -> Unit) {
        this
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete, { onError(it) })
            .disposeWhenCleared()
    }

    protected fun retryWithDelay(maxRetries: Int, delaySeconds: Long): (Observable<Throwable>) -> Observable<*> {
        return { errors ->
            errors
                .zipWith(Observable.range(1, maxRetries)) { error, retryCount ->
                    if (retryCount < maxRetries && error is ConnectException) {
                        Pair(error, retryCount)
                    } else {
                        throw error
                    }
                }
                .flatMap {
                    Observable.timer(delaySeconds, TimeUnit.SECONDS)
                }
        }
    }

    private fun IAuthenticationResult?.isTokenInvalid() : Boolean =
        this == null || this.expiresOn.before(Date())
}