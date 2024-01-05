package com.jlafshari.beerrecipegenerator.settings

data class RecipeDefaultSettings(var recipeSize: Double,
                                 var boilDurationMinutes: Int,
                                 var extractionEfficiency: Int,
                                 var mashThickness: Double,
                                 var grainTemperature: Int,
                                 var equipmentLossAmount: Float,
                                 var trubLossAmount: Float)