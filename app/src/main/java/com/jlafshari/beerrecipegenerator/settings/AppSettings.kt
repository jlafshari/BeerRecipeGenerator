package com.jlafshari.beerrecipegenerator.settings

import android.content.Context
import android.content.SharedPreferences
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipegenerator.HomebrewApiRequestHelper

object AppSettings {
    const val PREFERENCE_FILE_NAME = "app_setting_preference_file"

    lateinit var recipeDefaultSettings: RecipeDefaultSettings

    fun loadSettings(settings: SharedPreferences, requestHelper: HomebrewApiRequestHelper,
                     context: Context) {
        if (this::recipeDefaultSettings.isInitialized) return

        if (areAnySettingsMissing(settings)) getSettingsFromApi(requestHelper, context, settings)
        else loadSettingsFromAppPreferences(settings)
    }

    fun updateRecipeSize(recipeSize: Double, settings: SharedPreferences) {
        recipeDefaultSettings.size = recipeSize
        with(settings.edit()) {
            putFloat(RECIPE_SIZE, recipeSize.toFloat())
            apply()
        }
    }

    fun updateBoilDuration(boilDurationMinutes: Int, settings: SharedPreferences) {
        recipeDefaultSettings.boilDurationMinutes = boilDurationMinutes
        with(settings.edit()) {
            putInt(BOIL_DURATION_TIME, boilDurationMinutes)
            apply()
        }
    }

    fun updateExtractionEfficiency(extractionEfficiency: Int, settings: SharedPreferences) {
        recipeDefaultSettings.extractionEfficiency = extractionEfficiency
        with(settings.edit()) {
            putInt(EXTRACTION_EFFICIENCY, extractionEfficiency)
            apply()
        }
    }

    fun updateMashThickness(mashThickness: Double, settings: SharedPreferences) {
        recipeDefaultSettings.mashThickness = mashThickness
        with(settings.edit()) {
            putFloat(MASH_THICKNESS, mashThickness.toFloat())
            apply()
        }
    }

    private fun loadSettingsFromAppPreferences(settings: SharedPreferences) {
        val recipeSize = settings.getFloat(RECIPE_SIZE, 0F).toDouble()
        val boilTimeDuration = settings.getInt(BOIL_DURATION_TIME, 0)
        val extractionEfficiency = settings.getInt(EXTRACTION_EFFICIENCY, 0)
        val mashThickness = settings.getFloat(MASH_THICKNESS, 0F).toDouble()
        recipeDefaultSettings = RecipeDefaultSettings(recipeSize, boilTimeDuration, extractionEfficiency, mashThickness)
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
                    putFloat(MASH_THICKNESS, recipeDefaultSettings.mashThickness.toFloat())
                    apply()
                }
            }
        }
        requestHelper.getDefaultSettings(context, callBack)
    }

    private fun areAnySettingsMissing(settings: SharedPreferences) =
        !settings.contains(RECIPE_SIZE) || !settings.contains(BOIL_DURATION_TIME) ||
        !settings.contains(EXTRACTION_EFFICIENCY) || !settings.contains(MASH_THICKNESS)

    private const val RECIPE_SIZE = "recipe_size"
    private const val BOIL_DURATION_TIME = "boil_duration_time"
    private const val EXTRACTION_EFFICIENCY = "extraction_efficiency"
    private const val MASH_THICKNESS = "mash_thickness"
}