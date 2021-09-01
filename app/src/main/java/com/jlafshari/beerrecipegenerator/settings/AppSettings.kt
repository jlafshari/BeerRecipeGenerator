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

        if (areAnySettingsMissing(settings)) getSettingsFromApi(requestHelper, context, settings)
        else loadSettingsFromAppPreferences(settings)
    }

    private fun loadSettingsFromAppPreferences(settings: SharedPreferences) {
        val recipeSize = settings.getFloat(RECIPE_SIZE, 0F).toDouble()
        val boilTimeDuration = settings.getInt(BOIL_DURATION_TIME, 0)
        val extractionEfficiency = settings.getInt(EXTRACTION_EFFICIENCY, 0)
        recipeDefaultSettings = RecipeDefaultSettings(recipeSize, boilTimeDuration, extractionEfficiency)
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
                    putInt(BOIL_DURATION_TIME, recipeDefaultSettings.boilDurationMinutes)
                    putInt(EXTRACTION_EFFICIENCY, recipeDefaultSettings.extractionEfficiency)
                    apply()
                }
            }
        }
        requestHelper.getDefaultSettings(context, callBack)
    }

    private fun areAnySettingsMissing(settings: SharedPreferences) =
        !settings.contains(RECIPE_SIZE) || !settings.contains(BOIL_DURATION_TIME) ||
        !settings.contains(EXTRACTION_EFFICIENCY)

    private const val RECIPE_SIZE = "recipe_size"
    private const val BOIL_DURATION_TIME = "boil_duration_time"
    private const val EXTRACTION_EFFICIENCY = "extraction_efficiency"
}