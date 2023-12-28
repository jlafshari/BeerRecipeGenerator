package com.jlafshari.beerrecipecore

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class HopIngredient(val name: String,
                         var amount: Double,
                         var boilAdditionTime: Int?,
                         val hopId: String,
                         val use: HopUse,
                         val dryHopDayStart: Int?,
                         val dryHopDayEnd: Int?,
                         val whirlpoolDuration: Int?)