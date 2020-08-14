package com.jlafshari.beerrecipegenerator

import android.os.Build
import androidx.annotation.RequiresApi
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Recipe
import java.io.File

object MyRecipesHelper {
    private const val recipesFileName = "saved recipes.json"
    private val jacksonObjectMapper = jacksonObjectMapper()

    fun getRecipe(recipeId: String, externalFilesDirectory: File): Recipe? =
        getSavedRecipes(externalFilesDirectory).find { it.id == recipeId }

    fun getSavedRecipePreviews(externalFilesDirectory: File): List<RecipePreview> =
        getSavedRecipes(externalFilesDirectory)
            .map { RecipePreview(it.id, it.name, it.style.name) }

    fun saveRecipe(recipe: Recipe, externalFilesDirectory: File) {
        val recipesList = getSavedRecipes(externalFilesDirectory)
        recipesList.add(recipe)
        saveRecipes(recipesList, externalFilesDirectory)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteRecipe(recipeId: String, externalFilesDirectory: File) {
        val recipesList = getSavedRecipes(externalFilesDirectory)
        recipesList.removeIf { it.id == recipeId }
        saveRecipes(recipesList, externalFilesDirectory)
    }

    private fun saveRecipes(
        recipesList: MutableList<Recipe>,
        externalFilesDirectory: File
    ) {
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