package com.jlafshari.beerrecipecore

data class MashProfile(val mashSteps: List<MashStep>,
                       val grainTemperature: Int,
                       val mashStrikeWaterAmount: Float,
                       val mashStrikeWaterTemperature: Int,
                       val mashThickness: Float
)
