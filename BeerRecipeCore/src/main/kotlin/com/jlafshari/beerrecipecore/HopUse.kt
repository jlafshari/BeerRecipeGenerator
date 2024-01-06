package com.jlafshari.beerrecipecore

import com.fasterxml.jackson.annotation.JsonValue


enum class HopUse {
    Boil,
    DryHop,
    Whirlpool;

    @JsonValue
    fun toJson(): Int = ordinal
}
