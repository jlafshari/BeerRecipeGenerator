package com.jlafshari.beerrecipecore

data class MashProfile(val mashSteps: List<MashStep>,
                       val grainTemperature: Int,
                       val mashStrikeWaterAmount: Float,
                       val mashStrikeWaterTemperature: Int,
                       val mashThickness: Float
)

data class MashProfileForUpdate(val mashSteps: List<MashStep>,
                       val grainTemperature: Int,
                       val mashThickness: Float
)