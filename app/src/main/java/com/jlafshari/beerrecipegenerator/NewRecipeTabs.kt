package com.jlafshari.beerrecipegenerator

import androidx.fragment.app.Fragment

object NewRecipeTabs {
    val tabTitles = listOf("Beer Style", "Recipe Size")

    fun getTabTitle(tabPosition: Int): String =
        if (tabPosition < tabTitles.size) tabTitles[tabPosition] else tabTitles[0]

    fun getTabFragment(tabPosition: Int): Fragment = when (tabPosition) {
        0 -> BeerStyleFragment()
        1 -> RecipeSizeFragment()
        else -> BeerStyleFragment()
    }
}