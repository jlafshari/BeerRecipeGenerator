package com.jlafshari.beerrecipegenerator.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipecore.recipes.RecipeType

class RecipeViewModel : ViewModel() {
    private val _loadRecipePreviewsResponse = MutableLiveData<List<RecipePreview>>()
    val loadRecipePreviewsResponse: LiveData<List<RecipePreview>> = _loadRecipePreviewsResponse

    fun loadRecipePreviews() {
        _loadRecipePreviewsResponse.postValue(listOf(
            RecipePreview("1", "Brown", RecipeType.Ale, false, 1, 5.5F, 16)))
    }
}