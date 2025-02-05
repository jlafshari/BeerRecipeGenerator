package com.jlafshari.beerrecipegenerator.viewRecipe

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.batches.NewBatchInfo
import com.jlafshari.beerrecipecore.recipes.Recipe
import com.jlafshari.beerrecipecore.utility.DateUtility
import com.jlafshari.beerrecipegenerator.*
import com.jlafshari.beerrecipegenerator.batches.BatchViewModel
import com.jlafshari.beerrecipegenerator.databinding.ActivityRecipeViewBinding
import com.jlafshari.beerrecipegenerator.editRecipe.EditRecipeActivity
import com.jlafshari.beerrecipegenerator.recipes.RecipeViewModel
import com.jlafshari.beerrecipegenerator.srmColors.Colors
import com.jlafshari.beerrecipegenerator.viewBatch.BatchViewActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant

@AndroidEntryPoint
class RecipeViewActivity : AppCompatActivity() {
    private lateinit var mRecipe: Recipe

    private val recipeViewModel: RecipeViewModel by viewModels()
    private val batchViewModel: BatchViewModel by viewModels()

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

        binding.mashStepsRecyclerView.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        binding.mashStepsRecyclerView.adapter = MashStepListAdapter(emptyList(), this)

        binding.fermentationStepsRecyclerView.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        binding.fermentationStepsRecyclerView.adapter = FermentationStepListAdapter(emptyList(), this)

        binding.miscIngredientsRecyclerView.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false)
        binding.miscIngredientsRecyclerView.adapter = MiscIngredientListAdapter(emptyList(), this)

        binding.recipeBatchesRecyclerView.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false)
        binding.recipeBatchesRecyclerView.adapter = BatchListAdapter(emptyList()) {}

        val recipeId = intent.getStringExtra(Constants.EXTRA_VIEW_RECIPE)!!
        recipeViewModel.loadRecipeDetails(recipeId)

        recipeViewModel.loadRecipeDetailsResponse.observe(this@RecipeViewActivity) {
            if (it != null) {
                mRecipe = it
                loadRecipeView(binding)
                recipeViewModel.loadRecipeDetailsComplete()
            }
        }

        recipeViewModel.deleteRecipeResponse.observe(this@RecipeViewActivity) {
            if (it.isSuccessful) {
                onRecipeDeleted()
            }
        }

        batchViewModel.newBatchResponse.observe(this@RecipeViewActivity) {
            if (it != null) {
                batchViewModel.newBatchComplete()
                goToBatchView(it)
            }
        }
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
        val mashProfile = mRecipe.mashProfile
        binding.txtMashStrikeWater.text = getString(R.string.recipe_view_mash_strike_water,
            mashProfile.mashStrikeWaterAmount, mashProfile.mashStrikeWaterTemperature.toString())
        binding.txtSpargeWater.text = getString(R.string.recipe_view_sparge_water, mRecipe.spargeWaterAmount)
        binding.txtTotalWater.text = getString(R.string.recipe_view_total_water,
            mashProfile.mashStrikeWaterAmount + mRecipe.spargeWaterAmount)
        binding.mashStepsRecyclerView.adapter = MashStepListAdapter(mRecipe.mashProfile.mashSteps, this)
        if (mRecipe.fermentationSteps.isEmpty()) {
            binding.txtFermentationStepsHeading.visibility = GONE
        } else {
            binding.fermentationStepsRecyclerView.adapter =
                FermentationStepListAdapter(mRecipe.fermentationSteps, this)
        }
        if (mRecipe.miscellaneousIngredients.isEmpty()) {
            binding.txtMiscIngredientHeading.visibility = GONE
        } else {
            binding.miscIngredientsRecyclerView.adapter = MiscIngredientListAdapter(mRecipe.miscellaneousIngredients, this)
        }

        binding.recipeBatchesRecyclerView.adapter = BatchListAdapter(mRecipe.batches) { batchPreview ->
            goToBatchView(batchPreview.id)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_recipe_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_delete -> {
                deleteRecipe()
                true
            }

            R.id.action_edit -> {
                editRecipe()
                true
            }

            R.id.action_new_batch -> {
                newBatch()
                true
            }

            android.R.id.home -> {
                goToMainActivity()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun newBatch() {
        val currentTimestamp = DateUtility.getFormattedTimeStamp(Instant.now())
        batchViewModel.newBatch(NewBatchInfo(mRecipe.id, currentTimestamp))
    }

    private fun deleteRecipe() {
        recipeViewModel.deleteRecipe(mRecipe.id)
    }

    private fun onRecipeDeleted() {
        Toast.makeText(this, "Recipe deleted!", Toast.LENGTH_SHORT).show()

        goToMainActivity()
    }

    private fun goToMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
    }

    private fun editRecipe() {
        val editRecipeIntent = Intent(this, EditRecipeActivity::class.java)
        editRecipeIntent.putExtra(Constants.EXTRA_EDIT_RECIPE, mRecipe.id)
        editRecipeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(editRecipeIntent)
    }

    private fun goToBatchView(batchId: String) {
        val viewBatchIntent = Intent(this, BatchViewActivity::class.java)
        viewBatchIntent.putExtra(Constants.EXTRA_VIEW_BATCH, batchId)
        startActivity(viewBatchIntent)
    }
}