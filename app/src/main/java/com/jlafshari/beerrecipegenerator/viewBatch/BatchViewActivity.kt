package com.jlafshari.beerrecipegenerator.viewBatch

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jlafshari.beerrecipecore.batches.Batch
import com.jlafshari.beerrecipecore.utility.DateUtility
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.batches.BatchViewModel
import com.jlafshari.beerrecipegenerator.databinding.ActivityBatchViewBinding
import com.jlafshari.beerrecipegenerator.viewRecipe.RecipeViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BatchViewActivity : AppCompatActivity() {
    private lateinit var mBatch: Batch

    private val batchViewModel: BatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBatchViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val batchId = intent.getStringExtra(Constants.EXTRA_VIEW_BATCH)!!
        batchViewModel.loadBatchDetails(batchId)

        batchViewModel.loadBatchDetailsResponse.observe(this@BatchViewActivity) {
            if (it != null) {
                mBatch = it
                loadBatchView(binding)
                batchViewModel.loadBatchDetailsComplete()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_view_batch, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                goBackToRecipeView()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadBatchView(binding: ActivityBatchViewBinding) {
        with (binding.root) {
            val txtRecipeName = findViewById<TextView>(R.id.txtRecipeName)
            txtRecipeName.text = mBatch.recipe.name

            val txtBrewingDate = findViewById<TextView>(R.id.txtBrewingDate)
            txtBrewingDate.text = DateUtility.getFormattedDate(mBatch.brewingDate)
        }
    }

    private fun goBackToRecipeView() {
        val recipeViewIntent =
            Intent(this@BatchViewActivity, RecipeViewActivity::class.java)
        recipeViewIntent.putExtra(Constants.EXTRA_VIEW_RECIPE, mBatch.recipe.id)
        startActivity(recipeViewIntent)
    }
}