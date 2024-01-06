package com.jlafshari.beerrecipecore

data class RecipeUpdateInfo(var name: String,
                            var size: Double,
                            var fermentableIngredients: MutableList<FermentableIngredient>,
                            var hopIngredients: MutableList<HopIngredient>,
                            var yeastId: String,
                            var extractionEfficiency: Int,
                            var boilDurationMinutes: Int,
                            var equipmentLossAmount: Float,
                            var trubLossAmount: Float,
                            var mashProfile: MashProfileForUpdate,
                            var miscellaneousIngredients: MutableList<MiscellaneousIngredientForUpdate>,
                            var fermentationSteps: MutableList<FermentationStep>)