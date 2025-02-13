package com.jlafshari.beerrecipecore.recipes

import com.jlafshari.beerrecipecore.batches.BatchPreview
import com.jlafshari.beerrecipecore.FermentableIngredient
import com.jlafshari.beerrecipecore.FermentationStep
import com.jlafshari.beerrecipecore.HopIngredient
import com.jlafshari.beerrecipecore.MashProfile
import com.jlafshari.beerrecipecore.MiscellaneousIngredient
import com.jlafshari.beerrecipecore.RecipeProjectedOutcome
import com.jlafshari.beerrecipecore.YeastIngredient

data class Recipe(val id: String,
                  val size: Double,
                  val name: String,
                  val styleName: String?,
                  val projectedOutcome: RecipeProjectedOutcome,
                  val fermentableIngredients: List<FermentableIngredient>,
                  val hopIngredients: List<HopIngredient>,
                  val yeastIngredient: YeastIngredient,
                  val mashProfile: MashProfile,
                  val spargeWaterAmount: Float,
                  val fermentationSteps: List<FermentationStep>,
                  val miscellaneousIngredients: List<MiscellaneousIngredient>,
                  val batches: List<BatchPreview>,
                  val isVersioned: Boolean,
                  val versionNumber: Int,
                  val type: RecipeType,
                  val lastUpdatedDate: String?
)