package com.jlafshari.beerrecipegenerator.editRecipe

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.*
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.HomebrewApiRequestHelper
import com.jlafshari.beerrecipegenerator.R
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
        binding.hopEditRecyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false)
        setHopEditRecyclerView(emptyList())

        val recipeId = intent.getStringExtra(Constants.EXTRA_EDIT_RECIPE)
        loadRecipe(recipeId!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_recipe, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_cancel_edit) {
            cancelEditRecipe()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cancelEditRecipe() {
        goBackToRecipeView()
    }

    private fun grainAmountChangedListener(amount: Double, fermentableId: String) {
        val fermentableIngredient = mRecipeUpdateInfo.fermentableIngredients.find {
            it.fermentableId == fermentableId }!!
        fermentableIngredient.amount = amount
    }

    private fun hopAmountChangedListener(amount: Double, index: Int) {
        val hopIngredient = mRecipeUpdateInfo.hopIngredients[index]
        hopIngredient.amount = amount
    }

    private fun hopBoilAdditionTimeChangedListener(boilAdditionTime: Int, index: Int) {
        val hopIngredient = mRecipeUpdateInfo.hopIngredients[index]
        hopIngredient.boilAdditionTime = boilAdditionTime
    }

    private fun deleteGrainListener(fermentableId: String) {
        mRecipeUpdateInfo.fermentableIngredients.removeIf { it.fermentableId == fermentableId }
        setGrainEditRecyclerView(mRecipeUpdateInfo.fermentableIngredients)
    }

    private fun deleteHopListener(index: Int) {
        mRecipeUpdateInfo.hopIngredients.removeAt(index)
        setHopEditRecyclerView(mRecipeUpdateInfo.hopIngredients)
    }

    private fun loadRecipe(recipeId: String) {
        HomebrewApiRequestHelper.getRecipe(recipeId, this, object : VolleyCallBack {
            override fun onSuccess(json: String) {
                val recipe: Recipe = jacksonObjectMapper().readValue(json)
                mRecipeId = recipe.id
                mRecipeUpdateInfo = RecipeUpdateInfo(recipe.name, recipe.fermentableIngredients, recipe.hopIngredients)
                loadRecipeView()
            }

            override fun onUnauthorizedResponse() {
                AuthHelper.startLoginActivity(this@EditRecipeActivity)
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@EditRecipeActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadRecipeView() {
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
        setHopEditRecyclerView(mRecipeUpdateInfo.hopIngredients)
    }

    private fun setGrainEditRecyclerView(grainList: List<FermentableIngredient>) {
        binding.grainEditRecyclerView.adapter = GrainEditListAdapter(grainList, this,
            { a, f -> grainAmountChangedListener(a, f) },
            { f -> deleteGrainListener(f) })
    }

    private fun setHopEditRecyclerView(hopIngredients: List<HopIngredient>) {
        binding.hopEditRecyclerView.adapter = HopEditListAdapter(hopIngredients,
            { a, h -> hopAmountChangedListener(a, h) },
            { t, h -> hopBoilAdditionTimeChangedListener(t, h) },
            { h -> deleteHopListener(h) })
    }

    fun addGrain(view: View) {
        val addGrainIntent = Intent(this, AddGrainActivity::class.java)
        addGrainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        addGrainIntent.putExtra(Constants.EXTRA_ADD_GRAIN_GRAINS_TO_EXCLUDE,
            mRecipeUpdateInfo.fermentableIngredients.map { it.fermentableId }.toTypedArray())
        startActivity(addGrainIntent)
    }

    fun addHop(view: View) {
        val addHopIntent = Intent(this, AddHopActivity::class.java)
        addHopIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(addHopIntent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val fermentableId = intent?.getStringExtra(Constants.EXTRA_ADD_GRAIN)
        if (fermentableId != null) {
            addGrainToRecipe(fermentableId)
        }

        val hopId = intent?.getStringExtra(Constants.EXTRA_ADD_HOP)
        if (hopId != null) addHopToRecipe(hopId)
    }

    private fun addHopToRecipe(hopId: String) {
        HomebrewApiRequestHelper.getHop(hopId, this, object : VolleyCallBack {
            override fun onSuccess(json: String) {
                val hop = jacksonObjectMapper().readValue<Hop>(json)
                addHopIngredientToRecipe(hop)
            }

            override fun onUnauthorizedResponse() {
                AuthHelper.startLoginActivity(this@EditRecipeActivity)
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@EditRecipeActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
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

    private fun addHopIngredientToRecipe(hop: Hop) {
        val hopIngredient = HopIngredient(hop.name, 1.0, 1, hop.id)
        mRecipeUpdateInfo.hopIngredients.add(hopIngredient)
        setHopEditRecyclerView(mRecipeUpdateInfo.hopIngredients)
    }

    private fun addFermentableIngredientToRecipe(fermentable: Fermentable) {
        val fermentableIngredient = FermentableIngredient(1.0, fermentable.name, fermentable.id)
        mRecipeUpdateInfo.fermentableIngredients.add(fermentableIngredient)
        setGrainEditRecyclerView(mRecipeUpdateInfo.fermentableIngredients)
    }

    fun updateRecipe(view: View) {
        HomebrewApiRequestHelper.updateRecipe(mRecipeId, mRecipeUpdateInfo, this, object : VolleyCallBack {
            override fun onSuccess(json: String) {
                goBackToRecipeView()
            }

            override fun onUnauthorizedResponse() {
                AuthHelper.startLoginActivity(this@EditRecipeActivity)
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@EditRecipeActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun goBackToRecipeView() {
        val recipeViewIntent = Intent(this, RecipeViewActivity::class.java)
        recipeViewIntent.putExtra(Constants.EXTRA_VIEW_RECIPE, mRecipeId)
        startActivity(recipeViewIntent)
    }
}