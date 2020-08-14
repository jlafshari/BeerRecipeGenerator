package com.jlafshari.beerrecipegenerator

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Recipe
import java.io.File

object MyRecipesHelper {
    private const val recipesFileName = "saved recipes.json"
    private val jacksonObjectMapper = jacksonObjectMapper()

    fun getSavedRecipePreviews(externalFilesDirectory: File): List<RecipePreview> {
        return getSavedRecipes(externalFilesDirectory)
            .map { RecipePreview(it.name, it.style.name) }
    }

    fun saveRecipe(recipe: Recipe, externalFilesDirectory: File) {
        val recipesList = getSavedRecipes(externalFilesDirectory)
        recipesList.add(recipe)
        val recipesJsonToSave = jacksonObjectMapper.writeValueAsString(recipesList)
        val recipesFile = getSavedRecipesFile(externalFilesDirectory)
        recipesFile.writeText(recipesJsonToSave)
    }

    private fun getSavedRecipes(externalFilesDirectory: File): MutableList<Recipe> {
        val recipesFile = getSavedRecipesFile(externalFilesDirectory)
        var recipesList = mutableListOf<Recipe>()
        if (recipesFile.exists()) {
            val recipesJson = recipesFile.readText()
            recipesList = jacksonObjectMapper.readValue(recipesJson)
        }

        return recipesList
    }

    private fun getSavedRecipesFile(externalFilesDirectory: File): File =
        File(externalFilesDirectory, recipesFileName)
}