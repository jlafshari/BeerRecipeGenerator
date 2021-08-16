package com.jlafshari.beerrecipegenerator

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.RecipeUpdateInfo
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper
import com.jlafshari.beerrecipegenerator.ui.login.LoginActivity
import javax.inject.Inject

class HomebrewApiRequestHelper @Inject constructor() {

    fun getAllRecipes(context: Context, callBack: VolleyCallBack) {
        sendStandardAuthRequest(
            getUrl(getAllRecipesUrl, context), Request.Method.GET,
            context, callBack)
    }

    fun getRecipe(recipeId: String, context: Context, callBack: VolleyCallBack) {
        val url = getUrl("$recipeUrl/$recipeId", context)
        sendStandardAuthRequest(url, Request.Method.GET, context, callBack)
    }

    fun deleteRecipe(recipeId: String, context: Context, callBack: VolleyDeleteRequestCallBack) {
        val url = getUrl("$recipeUrl/$recipeId", context)
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object :
            StringRequest(
                Method.DELETE, url,
                {
                    callBack.onSuccess(context)
                },
                {
                    println(it)
                })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return getAuthHeader()
            }
        }
        queue.add(stringRequest)
    }

    fun generateRecipe(recipeGenerationInfo: RecipeGenerationInfo, context: Context, callBack: VolleyCallBack) {
        val url = getUrl(generateRecipeUrl, context)
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object :
            StringRequest(
                Method.POST, url,
                {
                    callBack.onSuccess(it)
                },
                {
                    println(it)
                    callBack.onError(it.toString())
                })
        {
            override fun getBodyContentType(): String = "application/json"

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray =
                jacksonObjectMapper().writeValueAsBytes(recipeGenerationInfo)

            override fun getHeaders(): MutableMap<String, String> = getAuthHeader()
        }
        queue.add(stringRequest)
    }

    fun updateRecipe(recipeId: String, recipeUpdateInfo: RecipeUpdateInfo, context: Context, callBack: VolleyCallBack) {
        val url = getUrl("$recipeUrl/$recipeId", context)
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object :
            StringRequest(
                Method.PATCH, url,
                {
                    callBack.onSuccess(it)
                },
                {
                    println(it)
                    callBack.onError(it.toString())
                })
        {
            override fun getBodyContentType(): String = "application/json"

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray =
                jacksonObjectMapper().writeValueAsBytes(recipeUpdateInfo)

            override fun getHeaders(): MutableMap<String, String> = getAuthHeader()
        }
        queue.add(stringRequest)
    }

    fun getAllStyles(context: Context, callBack: VolleyCallBack) {
        sendStandardAuthRequest(
            getUrl(getAllStylesUrl, context), Request.Method.GET,
            context, callBack)
    }

    fun getAllFermentables(context: Context, callBack: VolleyCallBack) {
        sendStandardAuthRequest(
            getUrl(getAllFermentablesUrl, context), Request.Method.GET,
            context, callBack)
    }

    fun getFermentable(fermentableId: String, context: Context, callBack: VolleyCallBack) {
        val url = getUrl("Fermentable/$fermentableId", context)
        sendStandardAuthRequest(url, Request.Method.GET, context, callBack)
    }

    fun getAllHops(context: Context, callBack: VolleyCallBack) {
        sendStandardAuthRequest(
            getUrl(getAllHopsUrl, context), Request.Method.GET,
            context, callBack)
    }

    fun getHop(hopId: String, context: Context, callBack: VolleyCallBack) {
        val url = getUrl("Hop/$hopId", context)
        sendStandardAuthRequest(url, Request.Method.GET, context, callBack)
    }

    fun getVolleyCallBack(context: Context, onSuccess: (json: String) -> Unit) : VolleyCallBack =
        object : VolleyCallBack {
            override fun onSuccess(json: String) {
                onSuccess(json)
            }

            override fun onUnauthorizedResponse() {
                startLoginActivity(context)
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

    private fun sendStandardAuthRequest(url: String, httpMethod: Int,
                                        context: Context, callBack: VolleyCallBack) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object :
            StringRequest(
                httpMethod, url,
                {
                    callBack.onSuccess(it)
                },
                {
                    if (it?.networkResponse?.statusCode == 401) {
                        callBack.onUnauthorizedResponse()
                    }
                    else {
                        callBack.onError(it.toString())
                    }
                    println(it)
                })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return getAuthHeader()
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }

    private fun getAuthHeader(): HashMap<String, String> {
        val accessToken = AuthHelper.getAccessToken()
        return hashMapOf("Authorization" to "Bearer $accessToken")
    }

    private fun getUrl(urlEnd: String, context: Context) : String {
        val baseUrl = context.resources.getString(R.string.homebrewApiBaseUrl)
        return "$baseUrl/$urlEnd"
    }

    private fun startLoginActivity(context: Context) {
        val loginActivityIntent = Intent(context, LoginActivity::class.java)
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