package com.jlafshari.beerrecipegenerator.editRecipe

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.*
import com.jlafshari.beerrecipecore.recipes.Recipe
import com.jlafshari.beerrecipegenerator.*
import com.jlafshari.beerrecipegenerator.databinding.ActivityEditRecipeBinding
import com.jlafshari.beerrecipegenerator.recipes.RecipeValidator
import com.jlafshari.beerrecipegenerator.settings.AppSettings
import com.jlafshari.beerrecipegenerator.viewRecipe.RecipeViewActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditRecipeActivity : AppCompatActivity() {
    private lateinit var mRecipeId: String
    private lateinit var mRecipeUpdateInfo: RecipeUpdateInfo
    private lateinit var binding: ActivityEditRecipeBinding

    @Inject
    lateinit var requestHelper: HomebrewApiRequestHelper
    @Inject
    lateinit var recipeValidator: RecipeValidator

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

    private fun cancelEditRecipe() = goBackToRecipeView()

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
        val callBack = requestHelper.getVolleyCallBack(this@EditRecipeActivity) { run {
            val recipe: Recipe = jacksonObjectMapper().readValue(it)
            recipe.hopIngredients[0].use.ordinal
            with(recipe) {
                mRecipeId = id
                mRecipeUpdateInfo = RecipeUpdateInfo(
                    name,
                    AppSettings.recipeDefaultSettings.recipeSize,
                    fermentableIngredients,
                    hopIngredients,
                    yeastIngredient.yeastId,
                    AppSettings.recipeDefaultSettings.extractionEfficiency,
                    AppSettings.recipeDefaultSettings.boilDurationMinutes,
                    AppSettings.recipeDefaultSettings.equipmentLossAmount,
                    AppSettings.recipeDefaultSettings.trubLossAmount,
                    MashProfileForUpdate(
                        mashProfile.mashSteps,
                        mashProfile.grainTemperature,
                        mashProfile.mashThickness
                    ),
                    recipe.miscellaneousIngredients
                        .map { MiscellaneousIngredientForUpdate(it.miscellaneousIngredientInfoId, it.amount) }
                        .toMutableList(),
                    recipe.fermentationSteps.toMutableList()
                )
            }
            loadRecipeView()
        }}
        requestHelper.getRecipe(recipeId, this, callBack)
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
        binding.grainEditRecyclerView.adapter = GrainEditListAdapter(
            grainList,
            { a, f -> grainAmountChangedListener(a, f) }
        ) { f -> deleteGrainListener(f) }
    }

    private fun setHopEditRecyclerView(hopIngredients: List<HopIngredient>) {
        binding.hopEditRecyclerView.adapter = HopEditListAdapter(hopIngredients,
            { a, h -> hopAmountChangedListener(a, h) },
            { t, h -> hopBoilAdditionTimeChangedListener(t, h) },
            { h -> deleteHopListener(h) })
    }

    @Suppress("UNUSED_PARAMETER")
    fun addGrain(view: View) {
        val addGrainIntent = Intent(this, AddGrainActivity::class.java)
        addGrainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        addGrainIntent.putExtra(Constants.EXTRA_ADD_GRAIN_GRAINS_TO_EXCLUDE,
            mRecipeUpdateInfo.fermentableIngredients.map { it.fermentableId }.toTypedArray())
        startActivity(addGrainIntent)
    }

    @Suppress("UNUSED_PARAMETER")
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
        val callBack = requestHelper.getVolleyCallBack(this@EditRecipeActivity) { run {
            val hop = jacksonObjectMapper().readValue<Hop>(it)
            addHopIngredientToRecipe(hop)
        }}
        requestHelper.getHop(hopId, this, callBack)
    }

    private fun addGrainToRecipe(fermentableId: String) {
        val callBack = requestHelper.getVolleyCallBack(this@EditRecipeActivity) { run {
            val fermentable = jacksonObjectMapper().readValue<Fermentable>(it)
            addFermentableIngredientToRecipe(fermentable)
        }}
        requestHelper.getFermentable(fermentableId, this, callBack)
    }

    private fun addHopIngredientToRecipe(hop: Hop) {
        val hopIngredient = HopIngredient(hop.name, 1.0, 1, hop.id, HopUse.Boil, null, null, null)
        mRecipeUpdateInfo.hopIngredients.add(hopIngredient)
        setHopEditRecyclerView(mRecipeUpdateInfo.hopIngredients)
    }

    private fun addFermentableIngredientToRecipe(fermentable: Fermentable) {
        val fermentableIngredient = FermentableIngredient(1.0, fermentable.name, fermentable.color, fermentable.id)
        mRecipeUpdateInfo.fermentableIngredients.add(fermentableIngredient)
        setGrainEditRecyclerView(mRecipeUpdateInfo.fermentableIngredients)
    }

    @Suppress("UNUSED_PARAMETER")
    fun updateRecipe(view: View) {
        val validationResult = recipeValidator.validateRecipeUpdateInfo(mRecipeUpdateInfo)
        if (!validationResult.succeeded) {
            binding.txtErrorMsg.text = validationResult.message
            return
        }

        val callBack = requestHelper.getVolleyCallBack(this@EditRecipeActivity) { goBackToRecipeView() }
        requestHelper.updateRecipe(mRecipeId, mRecipeUpdateInfo, this, callBack)
    }

    private fun goBackToRecipeView() {
        val recipeViewIntent = Intent(this, RecipeViewActivity::class.java)
        recipeViewIntent.putExtra(Constants.EXTRA_VIEW_RECIPE, mRecipeId)
        startActivity(recipeViewIntent)
    }
}