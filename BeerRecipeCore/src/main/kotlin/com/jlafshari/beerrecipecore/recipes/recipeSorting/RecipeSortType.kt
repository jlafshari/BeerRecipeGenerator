package com.jlafshari.beerrecipecore.recipes.recipeSorting

enum class RecipeSortType {
    None,
    Abv,
    Color,
    BatchCount
}

fun RecipeSortType.displayText(): String = recipeSortTypeEnumDisplayMap[this]!!

val recipeSortTypeEnumDisplayMap = mapOf(
    RecipeSortType.None to "-- Sort By --",
    RecipeSortType.Abv to "ABV",
    RecipeSortType.Color to "Color",
    RecipeSortType.BatchCount to "Batch Count"
)