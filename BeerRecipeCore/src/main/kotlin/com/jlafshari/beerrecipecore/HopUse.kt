package com.jlafshari.beerrecipecore


enum class HopUse {
    Boil,
    DryHop,
    Whirlpool
}

fun HopUse.displayText(): String = hopUseEnumDisplayMap[this]!!

val hopUseEnumDisplayMap = mapOf(
    HopUse.Boil to "Boil",
    HopUse.DryHop to "Dry Hop",
    HopUse.Whirlpool to "Whirlpool"
)