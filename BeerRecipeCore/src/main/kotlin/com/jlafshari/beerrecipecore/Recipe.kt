package com.jlafshari.beerrecipecore

data class Recipe(val id: String, val style: Style, val size: Double, val name: String, val abv: Double,
                  val colorSrm: Int)