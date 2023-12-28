package com.jlafshari.beerrecipegenerator.viewRecipe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Recipe
import com.jlafshari.beerrecipegenerator.*
import com.jlafshari.beerrecipegenerator.databinding.ActivityRecipeViewBinding
import com.jlafshari.beerrecipegenerator.editRecipe.EditRecipeActivity
import com.jlafshari.beerrecipegenerator.srmColors.Colors
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeViewActivity : AppCompatActivity() {
    private lateinit var mRecipe: Recipe

    @Inject
    lateinit var requestHelper: HomebrewApiRequestHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRecipeViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.grainRecyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        binding.grainRecyclerView.adapter = GrainListAdapter(emptyList(), this)
        binding.hopsRecyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        binding.hopsRecyclerView.adapter = HopListAdapter(emptyList(), this)

        val recipeId = intent.getStringExtra(Constants.EXTRA_VIEW_RECIPE)
        loadRecipe(recipeId!!, binding)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val mainActivityIntent = Intent(this@RecipeViewActivity, MainActivity::class.java)
                startActivity(mainActivityIntent)
            }
        })
    }

    private fun loadRecipeView(binding: ActivityRecipeViewBinding) {
        binding.txtRecipeViewName.text = mRecipe.name
        if (mRecipe.styleName != null) {
            binding.txtStyle.text = getString(R.string.recipe_view_style_name, mRecipe.styleName)
        } else {
            binding.txtStyle.visibility = GONE
        }
        binding.txtSize.text = getString(R.string.recipe_view_size, mRecipe.size.toString())
        binding.txtAbv.text =
            getString(R.string.recipe_view_abv, mRecipe.projectedOutcome.abv.toString())
        val srmColor: Int = mRecipe.projectedOutcome.colorSrm
        binding.txtColor.text = getString(R.string.recipe_view_color, srmColor.toString())
        binding.txtIbu.text = getString(R.string.recipe_view_ibu, mRecipe.projectedOutcome.ibu.toString())
        val color = Colors.getColor(srmColor)
        if (color != null) {
            binding.srmColorCardView.setCardBackgroundColor(color.rbgColor)
        }
        binding.txtOG.text = getString(R.string.originalGravity, mRecipe.projectedOutcome.originalGravity)
        binding.txtFG.text = getString(R.string.finalGravity, mRecipe.projectedOutcome.finalGravity)
        binding.grainRecyclerView.adapter = GrainListAdapter(mRecipe.fermentableIngredients, this)
        binding.hopsRecyclerView.adapter = HopListAdapter(mRecipe.hopIngredients, this)
        val yeastIngredient = mRecipe.yeastIngredient
        binding.txtYeast.text = getString(R.string.yeastIngredient, yeastIngredient.name, yeastIngredient.laboratory)
        binding.txtMashStrikeWater.text = getString(R.string.recipe_view_mash_strike_water, mRecipe.mashStrikeWaterAmount.toString())
    }

    private fun loadRecipe(recipeId: String, binding: ActivityRecipeViewBinding) {
        val callBack = requestHelper.getVolleyCallBack(this) { run {
            mRecipe = jacksonObjectMapper().readValue(it)
            loadRecipeView(binding)
        }}
        requestHelper.getRecipe(recipeId, this, callBack)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_recipe_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_delete) {
            deleteRecipe()
            return true
        }
        else if (id == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteRecipe() {
        requestHelper.deleteRecipe(mRecipe.id, this, object : VolleyDeleteRequestCallBack {
            override fun onSuccess(context: Context) {
                Toast.makeText(context, "Recipe deleted!", Toast.LENGTH_SHORT).show()

                val mainActivityIntent = Intent(context, MainActivity::class.java)
                startActivity(mainActivityIntent)
            }
        })
    }

    @Suppress("UNUSED_PARAMETER")
    fun editRecipe(view: View) {
        val editRecipeIntent = Intent(this, EditRecipeActivity::class.java)
        editRecipeIntent.putExtra(Constants.EXTRA_EDIT_RECIPE, mRecipe.id)
        editRecipeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(editRecipeIntent)
    }
}