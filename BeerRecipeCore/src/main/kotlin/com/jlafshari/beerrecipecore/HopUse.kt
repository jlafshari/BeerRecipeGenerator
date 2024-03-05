package com.jlafshari.beerrecipecore

import com.google.gson.annotations.SerializedName


enum class HopUse(val value: Int) {
    @SerializedName("0") Boil(0),
    @SerializedName("1") DryHop(1),
    @SerializedName("2") Whirlpool(2)
}
