package com.jlafshari.beerrecipegenerator.settings

data class RecipeDefaultSettings(var size: Double,
                                 val boilDurationMinutes: Int,
                                 var extractionEfficiency: Int,
                                 var mashThickness: Double)