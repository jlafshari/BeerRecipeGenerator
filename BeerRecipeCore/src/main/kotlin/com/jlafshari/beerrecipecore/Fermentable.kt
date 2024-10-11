package com.jlafshari.beerrecipecore

import java.io.Serializable

data class Fermentable(val id: String, val name: String, val notes: String, val color: Double,
    val maltCategory: String?) : Serializable