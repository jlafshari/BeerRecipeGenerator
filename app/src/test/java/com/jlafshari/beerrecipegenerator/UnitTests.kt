package com.jlafshari.beerrecipegenerator

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Style
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class UnitTests {
    @Test
    fun canLoadRecipeStyles() {

        val json = jacksonObjectMapper()
        val recipeStylesJSON = File("src/main/res/raw/recipe_styles.json").readText()
        val recipeStyles: List<Style> = json.readValue(recipeStylesJSON)
        println(recipeStyles)
        assertEquals(3, recipeStyles.count())
    }
}
