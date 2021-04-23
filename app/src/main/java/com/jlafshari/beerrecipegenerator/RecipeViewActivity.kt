package com.jlafshari.beerrecipegenerator

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Recipe
import com.jlafshari.beerrecipegenerator.databinding.ActivityRecipeViewBinding
import com.jlafshari.beerrecipegenerator.srmColors.Colors

class RecipeViewActivity : AppCompatActivity() {
    private var mRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRecipeViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeId = intent.getStringExtra(Constants.EXTRA_VIEW_RECIPE)
        loadRecipe(recipeId!!, binding)
    }

    private fun loadRecipeView(binding: ActivityRecipeViewBinding) {
        binding.txtRecipeName.text = mRecipe!!.name
        binding.txtStyle.text = getString(R.string.recipe_view_style_name, mRecipe!!.styleName)
        binding.txtSize.text = getString(R.string.recipe_view_size, mRecipe!!.size.toString())
        binding.txtAbv.text =
            getString(R.string.recipe_view_abv, mRecipe!!.projectedOutcome.abv.toString())
        val srmColor: Int = mRecipe!!.projectedOutcome.colorSrm
        binding.txtColor.text = getString(R.string.recipe_view_color, srmColor.toString())
        binding.srmColorCardView.setCardBackgroundColor(Colors.getColor(srmColor).rbgColor)
    }

    private fun loadRecipe(recipeId: String, binding: ActivityRecipeViewBinding) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET,
            "${resources.getString(R.string.recipeBaseUrl)}/$recipeId",
            {
                val json = jacksonObjectMapper()
                mRecipe = json.readValue(it)
                loadRecipeView(binding)
            },
            { println(it) })
        queue.add(stringRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_recipe_view, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_delete) {
            deleteRecipe()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun deleteRecipe() {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.DELETE,
            "${resources.getString(R.string.recipeBaseUrl)}/${mRecipe!!.id}",
            {
                Toast.makeText(this, "Recipe deleted!", Toast.LENGTH_SHORT).show()

                val mainActivityIntent = Intent(this, MainActivity::class.java)
                startActivity(mainActivityIntent)
            },
            { println(it) }
        )
        queue.add(stringRequest)
    }
}