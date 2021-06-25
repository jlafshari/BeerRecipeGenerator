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
import com.jlafshari.beerrecipecore.Fermentable
import com.jlafshari.beerrecipecore.FermentableIngredient
import com.jlafshari.beerrecipecore.Recipe
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.HomebrewApiRequestHelper
import com.jlafshari.beerrecipegenerator.VolleyCallBack
import com.jlafshari.beerrecipegenerator.databinding.ActivityEditRecipeBinding
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper

class EditRecipeActivity : AppCompatActivity() {
    private lateinit var mRecipe: Recipe
    private lateinit var binding: ActivityEditRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.grainEditRecyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        setGrainEditRecyclerView(emptyList())

        val recipeId = intent.getStringExtra(Constants.EXTRA_EDIT_RECIPE)
        loadRecipe(recipeId!!, binding)
    }

    private fun grainAmountChangedListener(amount: Double, fermentableId: String) {
        val fermentableIngredient = mRecipe.fermentableIngredients.find { it.fermentableId == fermentableId }!!
        fermentableIngredient.amount = amount
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
        setGrainEditRecyclerView(mRecipe.fermentableIngredients)
    }

    private fun setGrainEditRecyclerView(grainList: List<FermentableIngredient>) {
        binding.grainEditRecyclerView.adapter = GrainEditListAdapter(grainList, this) {
                a, f -> grainAmountChangedListener(a, f) }
    }

    fun addGrain(view: View) {
        val addGrainIntent = Intent(this, AddGrainActivity::class.java)
        addGrainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(addGrainIntent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val fermentableId = intent?.getStringExtra(Constants.EXTRA_ADD_GRAIN)
        if (fermentableId != null) {
            addGrainToRecipe(fermentableId)
        }
    }

    private fun addGrainToRecipe(fermentableId: String) {
        HomebrewApiRequestHelper.getFermentable(fermentableId, this, object : VolleyCallBack {
            override fun onSuccess(json: String) {
                val fermentable = jacksonObjectMapper().readValue<Fermentable>(json)
                addFermentableIngredientToRecipe(fermentable)
            }

            override fun onUnauthorizedResponse() {
                AuthHelper.startLoginActivity(this@EditRecipeActivity)
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@EditRecipeActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun addFermentableIngredientToRecipe(fermentable: Fermentable) {
        val fermentableIngredient = FermentableIngredient(1.0, fermentable.name, fermentable.id)
        mRecipe.fermentableIngredients.add(fermentableIngredient)
        setGrainEditRecyclerView(mRecipe.fermentableIngredients)
    }
}