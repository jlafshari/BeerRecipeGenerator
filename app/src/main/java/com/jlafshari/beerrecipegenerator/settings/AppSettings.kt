package com.jlafshari.beerrecipegenerator.settings

import android.content.Context
import android.content.SharedPreferences
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipegenerator.HomebrewApiRequestHelper

object AppSettings {
    lateinit var recipeDefaultSettings: RecipeDefaultSettings

    fun loadSettings(settings: SharedPreferences, requestHelper: HomebrewApiRequestHelper,
                     context: Context) {
        if (this::recipeDefaultSettings.isInitialized) return

        if (!settings.contains(RECIPE_SIZE)) getSettingsFromApi(requestHelper, context, settings)
        else loadSettingsFromAppPreferences(settings)
    }

    private fun loadSettingsFromAppPreferences(settings: SharedPreferences) {
        val recipeSize = settings.getFloat(RECIPE_SIZE, 0F).toDouble()
        recipeDefaultSettings = RecipeDefaultSettings(recipeSize)
    }

    private fun getSettingsFromApi(
        requestHelper: HomebrewApiRequestHelper,
        context: Context,
        settings: SharedPreferences
    ) {
        val callBack = requestHelper.getVolleyCallBack(context) {
            run {
                recipeDefaultSettings = jacksonObjectMapper().readValue(it)

                //save settings to preferences
                with(settings.edit()) {
                    putFloat(RECIPE_SIZE, recipeDefaultSettings.size.toFloat())
                    apply()
                }
            }
        }
        requestHelper.getDefaultSettings(context, callBack)
    }

    private const val RECIPE_SIZE = "recipe_size"
}