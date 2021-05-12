package com.jlafshari.beerrecipegenerator.newRecipe

import androidx.fragment.app.Fragment

object NewRecipeSteps {
    const val numberOfSteps = 6

    fun getTabFragment(tabPosition: Int): Fragment = when (tabPosition) {
        0 -> BeerStyleFragment()
        1 -> RecipeSizeFragment()
        2 -> AbvFragment()
        3 -> ColorFragment()
        4 -> BitternessFragment()
        5 -> GenerateRecipeFragment()
        else -> BeerStyleFragment()
    }
}