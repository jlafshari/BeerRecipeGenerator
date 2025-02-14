package com.jlafshari.beerrecipegenerator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object RecipeSearchFilterUtility {
    private const val RECIPE_SEARCH_FILTER_FILE_NAME = "recipeSearchFilter"

    fun loadRecipeSearchFilter(context: Context) : RecipeSearchFilter? {
        return try {
            context.openFileInput(RECIPE_SEARCH_FILTER_FILE_NAME).use { fis ->
                val objectInputStream = ObjectInputStream(fis)
                val recipeSearchFilter = objectInputStream.readObject() as RecipeSearchFilter
                objectInputStream.close()
                recipeSearchFilter
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveRecipeSearchFilter(recipeSearchFilter: RecipeSearchFilter, context: Context) {
        context.openFileOutput(RECIPE_SEARCH_FILTER_FILE_NAME, MODE_PRIVATE).use { fos ->
            val objectOutputStream = ObjectOutputStream(fos)
            objectOutputStream.writeObject(recipeSearchFilter)
            objectOutputStream.close()
        }
    }
}