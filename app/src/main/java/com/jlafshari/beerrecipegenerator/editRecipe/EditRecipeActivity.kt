package com.jlafshari.beerrecipegenerator.editRecipe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Recipe
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.HomebrewApiRequestHelper
import com.jlafshari.beerrecipegenerator.VolleyCallBack
import com.jlafshari.beerrecipegenerator.databinding.ActivityEditRecipeBinding
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper

class EditRecipeActivity : AppCompatActivity() {
    private lateinit var mRecipe: Recipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.grainEditRecyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        binding.grainEditRecyclerView.adapter = GrainEditListAdapter(emptyList(), this)

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
        binding.txtRecipeName.text.clear()
        binding.txtRecipeName.text.insert(0, mRecipe.name)
        binding.grainEditRecyclerView.adapter = GrainEditListAdapter(mRecipe.fermentableIngredients,
            this)
    }

    fun addGrain(view: View) {
        val addGrainIntent = Intent(this, AddGrainActivity::class.java)
        startActivity(addGrainIntent)
    }
}