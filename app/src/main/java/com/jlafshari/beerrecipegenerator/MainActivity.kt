package com.jlafshari.beerrecipegenerator

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipecore.utility.AbvUtility
import com.jlafshari.beerrecipegenerator.about.AboutActivity
import com.jlafshari.beerrecipegenerator.account.AccountActivity
import com.jlafshari.beerrecipegenerator.databinding.ActivityMainBinding
import com.jlafshari.beerrecipegenerator.newRecipe.NewRecipeWizardActivity
import com.jlafshari.beerrecipegenerator.recipes.RecipeListAdapter
import com.jlafshari.beerrecipegenerator.settings.AppSettings
import com.jlafshari.beerrecipegenerator.settings.SettingsActivity
import com.jlafshari.beerrecipegenerator.srmColors.Colors
import com.jlafshari.beerrecipegenerator.ui.login.AzureAuthHelper
import com.jlafshari.beerrecipegenerator.viewRecipe.RecipeViewActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var requestHelper: HomebrewApiRequestHelper
    private val abvValues = AbvUtility.getAbvRecipeSearchValues().map { it.toString() }.toTypedArray()
    private val srmColors = Colors.getSrmColors()

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
            initAbvSpinner(R.id.minAbvSpinner, 0, binding.root)
            initAbvSpinner(R.id.maxAbvSpinner, abvValues.size - 1, binding.root)

            initColorFilter(R.id.minColorBtn, R.id.selectedMinColorCardView, R.id.txtSelectedMinColor, 0)
            initColorFilter(R.id.maxColorBtn, R.id.selectedMaxColorCardView, R.id.txtSelectedMaxColor, srmColors.size - 1)

            loadSavedRecipePreviews(binding)

            loadSettings()

            val searchBtn = binding.root.findViewById<Button>(R.id.searchRecipeBtn)
            searchBtn.setOnClickListener {
                loadSavedRecipePreviews(binding)
            }

            initializeExpandSearchButton(binding)
        }
    }

    private fun showColorPickerDialog(selectedColorCardView: CardView, selectedColorTextView: TextView) {
        val recyclerView = RecyclerView(this)
        val colorPickerDialog = AlertDialog.Builder(this)
            .setTitle("Choose a color")
            .setView(recyclerView)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.setPadding(5)
        recyclerView.adapter = ColorPaletteDialogListAdapter(srmColors, R.layout.color_item_layout) { selectedColor ->
            val selectedSrmColor = srmColors.find { it.srmColor == selectedColor }!!
            selectedColorCardView.setCardBackgroundColor(selectedSrmColor.rbgColor)
            selectedColorTextView.text = selectedSrmColor.srmColor.toString()
            colorPickerDialog.dismiss()
        }

        colorPickerDialog.show()
    }

    private fun initColorFilter(colorPickerButtonId: Int, selectedColorCardViewId: Int, selectedColorTextId: Int,
                                startingColorIndex: Int) {
        val minColorPickerButton = findViewById<Button>(colorPickerButtonId)
        minColorPickerButton.setOnClickListener {
            showColorPickerDialog(
                findViewById(selectedColorCardViewId),
                findViewById(selectedColorTextId)
            )
        }
        val color = srmColors[startingColorIndex]
        findViewById<TextView>(selectedColorTextId).text = color.srmColor.toString()
        findViewById<CardView>(selectedColorCardViewId).setCardBackgroundColor(color.rbgColor)
    }

    private fun initAbvSpinner(id: Int, startingIndex: Int, view: View) {
        val spinner = view.findViewById<Spinner>(id)
        val adapter = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item,
            abvValues)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(startingIndex)
    }

    private fun initializeExpandSearchButton(binding: ActivityMainBinding) {
        with (binding.root) {
            val expandSearchBtn = findViewById<ImageButton>(R.id.expandSearchBtn)
            val hiddenSearchView = findViewById<ConstraintLayout>(R.id.recipeSearchLayout)
            val mainViewLayout = findViewById<ConstraintLayout>(R.id.mainViewLayout)
            expandSearchBtn.setOnClickListener {
                TransitionManager.beginDelayedTransition(mainViewLayout, AutoTransition())
                if (hiddenSearchView.visibility == View.VISIBLE) {
                    hiddenSearchView.visibility = View.GONE
                    expandSearchBtn.setImageResource(R.drawable.baseline_expand_more_24)
                } else {
                    hiddenSearchView.visibility = View.VISIBLE
                    expandSearchBtn.setImageResource(R.drawable.baseline_expand_less_24)
                }
            }
        }
    }

    private fun loadSavedRecipePreviews(binding: ActivityMainBinding) {
        with (binding.root) {
            val txtLoadingIndicator = findViewById<TextView>(R.id.txtLoadingIndicator)
            val txtRecipeCount = findViewById<TextView>(R.id.txtRecipeCount)
            val recipeRecyclerView = findViewById<RecyclerView>(R.id.recipeRecyclerView)
            recipeRecyclerView.visibility = View.INVISIBLE
            txtLoadingIndicator.visibility = View.VISIBLE
            val callBack = requestHelper.getVolleyCallBack(this@MainActivity) {
                run {
                    val recipePreviews: List<RecipePreview> = jacksonObjectMapper().readValue(it)
                    recipeRecyclerView.adapter =
                        RecipeListAdapter(recipePreviews) { recipePreview ->
                            recipePreviewClicked(recipePreview)
                        }
                    recipeRecyclerView.visibility = View.VISIBLE
                    txtLoadingIndicator.visibility = View.INVISIBLE
                    txtRecipeCount.text = context.getString(R.string.recipes_count, recipePreviews.size.toString())
                }
            }
            val abvCheckbox = findViewById<CheckBox>(R.id.chkAbvFilter)
            val minAbvSpinner = findViewById<Spinner>(R.id.minAbvSpinner)
            val abvMin = if (abvCheckbox.isChecked) abvValues[minAbvSpinner.selectedItemPosition] else null
            val maxAbvSpinner = findViewById<Spinner>(R.id.maxAbvSpinner)
            val abvMax = if (abvCheckbox.isChecked) abvValues[maxAbvSpinner.selectedItemPosition] else null

            val colorCheckBox = findViewById<CheckBox>(R.id.chkColorFilter)
            val colorMin = if (colorCheckBox.isChecked) findViewById<TextView>(R.id.txtSelectedMinColor).text.toString() else null
            val colorMax = if (colorCheckBox.isChecked) findViewById<TextView>(R.id.txtSelectedMaxColor).text.toString() else null

            val aleChecked = findViewById<CheckBox>(R.id.chkAle).isChecked
            val lagerChecked = findViewById<CheckBox>(R.id.chkLager).isChecked
            val yeastType = if (aleChecked && !lagerChecked) "ale"
                else if (!aleChecked && lagerChecked) "lager"
                else null

            requestHelper.getAllRecipes(this@MainActivity, abvMin, abvMax, colorMin, colorMax, yeastType, callBack)
        }
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
