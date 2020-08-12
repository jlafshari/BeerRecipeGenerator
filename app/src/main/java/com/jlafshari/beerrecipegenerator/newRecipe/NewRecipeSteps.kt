package com.jlafshari.beerrecipegenerator.newRecipe

import androidx.fragment.app.Fragment

object NewRecipeSteps {
    const val numberOfSteps = 2

    fun getTabFragment(tabPosition: Int): Fragment = when (tabPosition) {
        0 -> BeerStyleFragment()
        1 -> RecipeSizeFragment()
        else -> BeerStyleFragment()
    }
}