package com.jlafshari.beerrecipegenerator

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Recipe
import com.jlafshari.beerrecipegenerator.databinding.ActivityMainBinding
import com.jlafshari.beerrecipegenerator.newRecipe.NewRecipeWizardActivity
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper
import com.jlafshari.beerrecipegenerator.viewRecipe.RecipeViewActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val recipeRecyclerView = binding.root.findViewById<RecyclerView>(R.id.recipeRecyclerView)
        recipeRecyclerView.layoutManager =
            LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )
        recipeRecyclerView.adapter = RecipeListAdapter(emptyList()) { recipePreview -> recipePreviewClicked(recipePreview) }

        loadSavedRecipePreviews(recipeRecyclerView)
    }

    private fun loadSavedRecipePreviews(recipeRecyclerView: RecyclerView) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object :
            StringRequest(
            Method.GET, resources.getString(R.string.getAllRecipesUrl),
            {
                val json = jacksonObjectMapper()
                val recipes: List<Recipe> = json.readValue(it)
                val recipePreviews = recipes.map { r -> RecipePreview(r.id, r.name, r.styleName) }
                recipeRecyclerView.adapter = RecipeListAdapter(recipePreviews) { recipePreview -> recipePreviewClicked(recipePreview) }
            },
            {
                println(it)
            })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    val accessToken = AuthHelper.sessionClient?.tokens?.accessToken
                    headers["Authorization"] = "Bearer $accessToken"
                    return headers
                }
            }
        queue.add(stringRequest)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun newRecipe(view: View) {
        val newRecipeIntent = Intent(this, NewRecipeWizardActivity::class.java)
        startActivity(newRecipeIntent)
    }

    private fun recipePreviewClicked(recipePreview: RecipePreview) {
        val recipeViewIntent = Intent(this, RecipeViewActivity::class.java)
        recipeViewIntent.putExtra(Constants.EXTRA_VIEW_RECIPE, recipePreview.id)
        startActivity(recipeViewIntent)
    }
}
