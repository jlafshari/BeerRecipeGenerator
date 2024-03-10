package com.jlafshari.beerrecipegenerator.settings

import android.content.SharedPreferences

object AppSettings {
    const val PREFERENCE_FILE_NAME = "app_setting_preference_file"

    lateinit var recipeDefaultSettings: RecipeDefaultSettings

    fun loadSettings(settings: SharedPreferences, apiRecipeDefaultSettings: RecipeDefaultSettings) {
        if (this::recipeDefaultSettings.isInitialized) return

        if (areAnySettingsMissing(settings)) useSettingsFromApi(apiRecipeDefaultSettings, settings)
        else loadSettingsFromAppPreferences(settings)
    }

    fun updateRecipeSize(recipeSize: Double, settings: SharedPreferences) {
        recipeDefaultSettings.recipeSize = recipeSize
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

    fun updateGrainTemperature(grainTemperature: Int, settings: SharedPreferences) {
        recipeDefaultSettings.grainTemperature = grainTemperature
        with(settings.edit()) {
            putInt(GRAIN_TEMPERATURE, grainTemperature)
            apply()
        }
    }

    fun updateEquipmentLoss(equipmentLoss: Float, settings: SharedPreferences) {
        recipeDefaultSettings.equipmentLossAmount = equipmentLoss
        with(settings.edit()) {
            putFloat(EQUIPMENT_LOSS, equipmentLoss)
            apply()
        }
    }

    fun updateTrubLoss(trubLoss: Float, settings: SharedPreferences) {
        recipeDefaultSettings.trubLossAmount = trubLoss
        with(settings.edit()) {
            putFloat(TRUB_LOSS, trubLoss)
            apply()
        }
    }

    private fun loadSettingsFromAppPreferences(settings: SharedPreferences) {
        val recipeSize = settings.getFloat(RECIPE_SIZE, 0F).toDouble()
        val boilTimeDuration = settings.getInt(BOIL_DURATION_TIME, 0)
        val extractionEfficiency = settings.getInt(EXTRACTION_EFFICIENCY, 0)
        val mashThickness = settings.getFloat(MASH_THICKNESS, 0F).toDouble()
        val grainTemperature = settings.getInt(GRAIN_TEMPERATURE, 0)
        val equipmentLoss = settings.getFloat(EQUIPMENT_LOSS, 0F)
        val trubLoss = settings.getFloat(TRUB_LOSS, 0F)
        recipeDefaultSettings = RecipeDefaultSettings(recipeSize, boilTimeDuration, extractionEfficiency, mashThickness,
            grainTemperature, equipmentLoss, trubLoss)
    }

    private fun useSettingsFromApi(
        apiRecipeDefaultSettings: RecipeDefaultSettings,
        settings: SharedPreferences
    ) {
        recipeDefaultSettings = apiRecipeDefaultSettings

        //save settings to preferences
        with(settings.edit()) {
            putFloat(RECIPE_SIZE, recipeDefaultSettings.recipeSize.toFloat())
            putInt(BOIL_DURATION_TIME, recipeDefaultSettings.boilDurationMinutes)
            putInt(EXTRACTION_EFFICIENCY, recipeDefaultSettings.extractionEfficiency)
            putFloat(MASH_THICKNESS, recipeDefaultSettings.mashThickness.toFloat())
            putInt(GRAIN_TEMPERATURE, recipeDefaultSettings.grainTemperature)
            putFloat(EQUIPMENT_LOSS, recipeDefaultSettings.equipmentLossAmount)
            putFloat(TRUB_LOSS, recipeDefaultSettings.trubLossAmount)
            apply()
        }
    }

    private fun areAnySettingsMissing(settings: SharedPreferences) =
        !settings.contains(RECIPE_SIZE) || !settings.contains(BOIL_DURATION_TIME) ||
        !settings.contains(EXTRACTION_EFFICIENCY) || !settings.contains(MASH_THICKNESS) ||
        !settings.contains(GRAIN_TEMPERATURE) || !settings.contains(EQUIPMENT_LOSS) ||
        !settings.contains(TRUB_LOSS)

    private const val RECIPE_SIZE = "recipe_size"
    private const val BOIL_DURATION_TIME = "boil_duration_time"
    private const val EXTRACTION_EFFICIENCY = "extraction_efficiency"
    private const val MASH_THICKNESS = "mash_thickness"
    private const val GRAIN_TEMPERATURE = "grain_temperature"
    private const val EQUIPMENT_LOSS = "equipment_loss"
    private const val TRUB_LOSS = "trub_loss"
}