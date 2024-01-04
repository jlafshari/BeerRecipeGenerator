package com.jlafshari.beerrecipegenerator

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipegenerator.about.AboutActivity
import com.jlafshari.beerrecipegenerator.account.AccountActivity
import com.jlafshari.beerrecipegenerator.databinding.ActivityMainBinding
import com.jlafshari.beerrecipegenerator.newRecipe.NewRecipeWizardActivity
import com.jlafshari.beerrecipegenerator.recipes.RecipeListAdapter
import com.jlafshari.beerrecipegenerator.settings.AppSettings
import com.jlafshari.beerrecipegenerator.settings.SettingsActivity
import com.jlafshari.beerrecipegenerator.ui.login.AzureAuthHelper
import com.jlafshari.beerrecipegenerator.viewRecipe.RecipeViewActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var requestHelper: HomebrewApiRequestHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        AzureAuthHelper.isUserSignedIn(this) {
            val recipeRecyclerView =
                binding.root.findViewById<RecyclerView>(R.id.recipeRecyclerView)
            recipeRecyclerView.layoutManager =
                LinearLayoutManager(
                    this,
                    RecyclerView.VERTICAL,
                    false
                )
            recipeRecyclerView.adapter =
                RecipeListAdapter(emptyList()) { recipePreview -> recipePreviewClicked(recipePreview) }

            val txtLoadingIndicator = binding.root.findViewById<TextView>(R.id.txtLoadingIndicator)
            loadSavedRecipePreviews(recipeRecyclerView, txtLoadingIndicator)

            loadSettings()
        }
    }

    private fun loadSavedRecipePreviews(
        recipeRecyclerView: RecyclerView,
        txtLoadingIndicator: TextView
    ) {
        recipeRecyclerView.visibility = View.INVISIBLE
        txtLoadingIndicator.visibility = View.VISIBLE
        val callBack = requestHelper.getVolleyCallBack(this@MainActivity) { run {
            val recipePreviews: List<RecipePreview> = jacksonObjectMapper().readValue(it)
            recipeRecyclerView.adapter = RecipeListAdapter(recipePreviews) { recipePreview -> recipePreviewClicked(recipePreview) }
            recipeRecyclerView.visibility = View.VISIBLE
            txtLoadingIndicator.visibility = View.INVISIBLE
        }}
        requestHelper.getAllRecipes(this, callBack)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> showSettings()
        R.id.action_account -> showAccount()
        R.id.action_about -> showAbout()
        else -> super.onOptionsItemSelected(item)
    }

    private fun showAbout(): Boolean {
        val aboutIntent = Intent(this, AboutActivity::class.java)
        startActivity(aboutIntent)
        return true
    }

    private fun showAccount(): Boolean {
        val accountIntent = Intent(this, AccountActivity::class.java)
        startActivity(accountIntent)
        return true
    }

    private fun showSettings(): Boolean {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsIntent)
        return true
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

    private fun loadSettings() {
        val settings = getSharedPreferences(AppSettings.PREFERENCE_FILE_NAME, MODE_PRIVATE)
        AppSettings.loadSettings(settings, requestHelper, this)
    }
}
