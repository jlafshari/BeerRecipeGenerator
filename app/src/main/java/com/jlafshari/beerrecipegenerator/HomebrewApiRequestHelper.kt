package com.jlafshari.beerrecipegenerator

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipegenerator.ui.login.AzureAuthHelper
import com.jlafshari.beerrecipegenerator.ui.login.AzureLoginActivity
import javax.inject.Inject

class HomebrewApiRequestHelper @Inject constructor() {

    fun getAllStyles(context: Context, callBack: VolleyCallBack) =
        sendStandardAuthGetRequest(getUrl(getAllStylesUrl, context), context, callBack)

    fun getAllFermentables(context: Context, callBack: VolleyCallBack) =
        sendStandardAuthGetRequest(getUrl(getAllFermentablesUrl, context), context, callBack)

    fun getFermentable(fermentableId: String, context: Context, callBack: VolleyCallBack) {
        val url = getUrl("Fermentable/$fermentableId", context)
        sendStandardAuthGetRequest(url, context, callBack)
    }

    fun getAllHops(context: Context, callBack: VolleyCallBack) =
        sendStandardAuthGetRequest(getUrl(getAllHopsUrl, context), context, callBack)

    fun getDefaultSettings(context: Context, callBack: VolleyCallBack) {
        val url = getUrl(getUserSettings, context)
        sendStandardAuthGetRequest(url, context, callBack)
    }

    fun getVolleyCallBack(context: Context, onSuccess: (json: String) -> Unit) : VolleyCallBack =
        object : VolleyCallBack {
            override fun onSuccess(json: String) = onSuccess(json)

            override fun onUnauthorizedResponse() = startLoginActivity(context)

            override fun onError(errorMessage: String) =
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }

    private fun sendStandardAuthGetRequest(url: String, context: Context, callBack: VolleyCallBack) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object :
            StringRequest(Method.GET, url,
                {
                    callBack.onSuccess(it)
                },
                {
                    if (it?.networkResponse?.statusCode == 401) {
                        callBack.onUnauthorizedResponse()
                    }
                    else {
                        val errorMessage = jacksonObjectMapper().readValue<HomebrewApiError>(it.networkResponse.data)
                        Log.e("HomebrewApi", errorMessage.message)
                        callBack.onError(errorMessage.message)
                    }
                    println(it)
                })
        {
            override fun getHeaders(): MutableMap<String, String> = getAuthHeader()
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }

    private fun getAuthHeader(): HashMap<String, String> {
        val accessToken = AzureAuthHelper.getAccessToken()
        return hashMapOf("Authorization" to "Bearer $accessToken")
    }

    private fun getUrl(urlEnd: String, context: Context) : String {
        val httpScheme = context.resources.getString(R.string.homebrewApiHttpScheme)
        val baseAuthority = context.resources.getString(R.string.homebrewApiBaseDomain)
        return "$httpScheme://$baseAuthority/$urlEnd"
    }

    private fun startLoginActivity(context: Context) {
        val loginActivityIntent = Intent(context, AzureLoginActivity::class.java)
        loginActivityIntent.putExtra(Constants.EXTRA_SIGN_OUT, true)
        context.startActivity(loginActivityIntent)
    }

    companion object {
        private const val getAllRecipesUrl = "Recipe/GetAll"
        private const val recipeUrl = "Recipe"
        private const val generateRecipeUrl = "Recipe/GenerateRecipe"
        private const val getAllStylesUrl = "Style/GetAll"
        private const val getAllFermentablesUrl = "Fermentable/GetAll"
        private const val getAllHopsUrl = "Hop/GetAll"
        private const val getUserSettings = "UserSettings/GetSettings"
    }
}

interface VolleyCallBack {
    fun onSuccess(json: String)
    fun onUnauthorizedResponse()
    fun onError(errorMessage: String)
}

interface VolleyDeleteRequestCallBack {
    fun onSuccess(context: Context)
}