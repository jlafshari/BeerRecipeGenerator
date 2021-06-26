package com.jlafshari.beerrecipegenerator.editRecipe

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.jlafshari.beerrecipecore.RecipeUpdateInfo
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.HomebrewApiRequestHelper
import com.jlafshari.beerrecipegenerator.VolleyCallBack
import com.jlafshari.beerrecipegenerator.databinding.ActivityEditRecipeBinding
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper
import com.jlafshari.beerrecipegenerator.viewRecipe.RecipeViewActivity

class EditRecipeActivity : AppCompatActivity() {
    private lateinit var mRecipeId: String
    private lateinit var mRecipeUpdateInfo: RecipeUpdateInfo
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
        val fermentableIngredient = mRecipeUpdateInfo.fermentableIngredients.find {
            it.fermentableId == fermentableId }!!
        fermentableIngredient.amount = amount
    }

    private fun deleteGrainListener(fermentableId: String) {
        mRecipeUpdateInfo.fermentableIngredients.removeIf { it.fermentableId == fermentableId }
        setGrainEditRecyclerView(mRecipeUpdateInfo.fermentableIngredients)
    }

    private fun loadRecipe(recipeId: String, binding: ActivityEditRecipeBinding) {
        HomebrewApiRequestHelper.getRecipe(recipeId, this, object : VolleyCallBack {
            override fun onSuccess(json: String) {
                val recipe: Recipe = jacksonObjectMapper().readValue(json)
                mRecipeId = recipe.id
                mRecipeUpdateInfo = RecipeUpdateInfo(recipe.name, recipe.fermentableIngredients)
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
        binding.txtRecipeName.text.insert(0, mRecipeUpdateInfo.name)
        binding.txtRecipeName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(recipeNameEditText: Editable?) {
                mRecipeUpdateInfo.name = recipeNameEditText.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        setGrainEditRecyclerView(mRecipeUpdateInfo.fermentableIngredients)
    }

    private fun setGrainEditRecyclerView(grainList: List<FermentableIngredient>) {
        binding.grainEditRecyclerView.adapter = GrainEditListAdapter(grainList, this,
            { a, f -> grainAmountChangedListener(a, f) },
            { f -> deleteGrainListener(f) })
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
        mRecipeUpdateInfo.fermentableIngredients.add(fermentableIngredient)
        setGrainEditRecyclerView(mRecipeUpdateInfo.fermentableIngredients)
    }

    fun updateRecipe(view: View) {
        val recipeUpdateInfo = RecipeUpdateInfo(mRecipeUpdateInfo.name, mRecipeUpdateInfo.fermentableIngredients)

        HomebrewApiRequestHelper.updateRecipe(mRecipeId, recipeUpdateInfo, this, object : VolleyCallBack {
            override fun onSuccess(json: String) {
                val recipeViewIntent = Intent(this@EditRecipeActivity, RecipeViewActivity::class.java)
                recipeViewIntent.putExtra(Constants.EXTRA_VIEW_RECIPE, mRecipeId)
                startActivity(recipeViewIntent)
            }

            override fun onUnauthorizedResponse() {
                AuthHelper.startLoginActivity(this@EditRecipeActivity)
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@EditRecipeActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
}