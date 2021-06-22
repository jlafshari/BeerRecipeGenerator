package com.jlafshari.beerrecipegenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Recipe
import com.jlafshari.beerrecipegenerator.databinding.ActivityEditRecipeBinding
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper

class EditRecipeActivity : AppCompatActivity() {
    private var mRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeId = intent.getStringExtra(Constants.EXTRA_EDIT_RECIPE)
        loadRecipe(recipeId!!, binding)
    }

    private fun loadRecipe(recipeId: String, binding: ActivityEditRecipeBinding) {
        HomebrewApiRequestHelper.getRecipe(recipeId, this, object : VolleyCallBack {
            override fun onSuccess(json: String) {
                mRecipe = jacksonObjectMapper().readValue(json)
                loadRecipeView(binding)
            }

            override fun onUnauthorizedResponse() {
                AuthHelper.startLoginActivity(this@EditRecipeActivity)
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@EditRecipeActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadRecipeView(binding: ActivityEditRecipeBinding) {
        binding.txtRecipeName.text = mRecipe!!.name
    }
}