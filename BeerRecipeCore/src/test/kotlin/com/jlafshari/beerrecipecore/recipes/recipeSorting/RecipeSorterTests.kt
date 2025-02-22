package com.jlafshari.beerrecipecore.recipes.recipeSorting

import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipecore.recipes.RecipeType
import org.junit.Assert.assertEquals
import org.junit.Test

class RecipeSorterTests {

    private val recipes = listOf(
        RecipePreview("1", "Recipe1", RecipeType.Ale, true, 1,
            5.0f, 10, "2025-02-22T01:10:22.061Z", 1),
        RecipePreview("2", "Recipe2", RecipeType.Ale, true, 1,
            4.0f, 20, "2025-03-12T01:10:22.061Z", 2),
        RecipePreview("3", "Recipe3", RecipeType.Ale, true, 1,
            6.0f, 30, "2025-04-18T01:10:22.061Z", 3)
    )

    @Test
    fun `sortRecipes by Abv ascending`() {
        val sortedRecipes = RecipeSorter.sortRecipes(recipes, RecipeSortType.Abv, true)
        assertEquals(listOf(recipes[1], recipes[0], recipes[2]), sortedRecipes)
    }

    @Test
    fun `sortRecipes by Abv descending`() {
        val sortedRecipes = RecipeSorter.sortRecipes(recipes, RecipeSortType.Abv, false)
        assertEquals(listOf(recipes[2], recipes[0], recipes[1]), sortedRecipes)
    }

    @Test
    fun `sortRecipes by Color ascending`() {
        val sortedRecipes = RecipeSorter.sortRecipes(recipes, RecipeSortType.Color, true)
        assertEquals(listOf(recipes[0], recipes[1], recipes[2]), sortedRecipes)
    }

    @Test
    fun `sortRecipes by Color descending`() {
        val sortedRecipes = RecipeSorter.sortRecipes(recipes, RecipeSortType.Color, false)
        assertEquals(listOf(recipes[2], recipes[1], recipes[0]), sortedRecipes)
    }

    @Test
    fun `sortRecipes by BatchCount ascending`() {
        val sortedRecipes = RecipeSorter.sortRecipes(recipes, RecipeSortType.BatchCount, true)
        assertEquals(listOf(recipes[0], recipes[1], recipes[2]), sortedRecipes)
    }

    @Test
    fun `sortRecipes by BatchCount descending`() {
        val sortedRecipes = RecipeSorter.sortRecipes(recipes, RecipeSortType.BatchCount, false)
        assertEquals(listOf(recipes[2], recipes[1], recipes[0]), sortedRecipes)
    }

    @Test
    fun `sortRecipes with None sort type`() {
        val sortedRecipes = RecipeSorter.sortRecipes(recipes, RecipeSortType.None, true)
        assertEquals(recipes, sortedRecipes)
    }
}