package com.jlafshari.beerrecipegenerator

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper

object HomebrewApiRequestHelper {
    fun getAllRecipes(context: Context, callBack: VolleyCallBack) {
        sendStandardAuthRequest(context.resources.getString(R.string.getAllRecipesUrl), Request.Method.GET,
            context, callBack)
    }

    fun getRecipe(recipeId: String, context: Context, callBack: VolleyCallBack) {
        val url = "${context.resources.getString(R.string.recipeBaseUrl)}/$recipeId"
        sendStandardAuthRequest(url, Request.Method.GET, context, callBack)
    }

    fun deleteRecipe(recipeId: String, context: Context, callBack: VolleyDeleteRequestCallBack) {
        val url = "${context.resources.getString(R.string.recipeBaseUrl)}/$recipeId"
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
        val url = context.resources.getString(R.string.generateRecipeUrl)
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object :
            StringRequest(
                Method.POST, url,
                {
                    callBack.onSuccess(it)
                },
                {
                    println(it)
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

    fun getAllStyles(context: Context, callBack: VolleyCallBack) {
        sendStandardAuthRequest(context.resources.getString(R.string.getAllStylesUrl), Request.Method.GET,
            context, callBack)
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
                    println(it)
                })
        {
            override fun getHeaders(): MutableMap<String, String> {
                return getAuthHeader()
            }
        }
        queue.add(stringRequest)
    }

    private fun getAuthHeader(): HashMap<String, String> {
        val accessToken = AuthHelper.sessionClient!!.tokens.accessToken!!
        return hashMapOf("Authorization" to "Bearer $accessToken")
    }
}

interface VolleyCallBack {
    fun onSuccess(json: String)
}

interface VolleyDeleteRequestCallBack {
    fun onSuccess(context: Context)
}