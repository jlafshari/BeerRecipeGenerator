package com.jlafshari.beerrecipegenerator

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.jlafshari.beerrecipecore.Fermentable
import com.jlafshari.beerrecipecore.Hop
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipecore.recipes.recipeSorting.RecipeSortType
import com.jlafshari.beerrecipecore.recipes.recipeSorting.RecipeSorter.sortRecipes
import com.jlafshari.beerrecipecore.recipes.recipeSorting.displayText
import com.jlafshari.beerrecipecore.utility.AbvUtility
import com.jlafshari.beerrecipecore.utility.LastUpdatedUtility
import com.jlafshari.beerrecipegenerator.about.AboutActivity
import com.jlafshari.beerrecipegenerator.account.AccountActivity
import com.jlafshari.beerrecipegenerator.batches.BatchesInProgressAdapter
import com.jlafshari.beerrecipegenerator.batches.BatchViewModel
import com.jlafshari.beerrecipegenerator.databinding.ActivityMainBinding
import com.jlafshari.beerrecipegenerator.editRecipe.AddGrainActivity
import com.jlafshari.beerrecipegenerator.editRecipe.AddHopActivity
import com.jlafshari.beerrecipegenerator.editRecipe.IngredientViewModel
import com.jlafshari.beerrecipegenerator.newRecipe.NewRecipeWizardActivity
import com.jlafshari.beerrecipegenerator.recipes.RecipeListAdapter
import com.jlafshari.beerrecipegenerator.recipes.RecipeViewModel
import com.jlafshari.beerrecipegenerator.settings.AppSettings
import com.jlafshari.beerrecipegenerator.settings.RecipeDefaultSettings
import com.jlafshari.beerrecipegenerator.settings.SettingsActivity
import com.jlafshari.beerrecipegenerator.srmColors.Colors
import com.jlafshari.beerrecipegenerator.login.AzureAuthHelper
import com.jlafshari.beerrecipegenerator.viewBatch.BatchViewActivity
import com.jlafshari.beerrecipegenerator.viewRecipe.RecipeViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityMainBinding
    private var recipes: List<RecipePreview> = emptyList()

    private val abvValues = AbvUtility.getAbvRecipeSearchValues().map { it.toString() }.toTypedArray()
    private val srmColors = Colors.getSrmColors()
    private val fermentablesToSearch = mutableListOf<Fermentable>()
    private val hopsToSearch = mutableListOf<Hop>()
    private lateinit var batchesInProgressAdapter: BatchesInProgressAdapter
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val ingredientViewModel: IngredientViewModel by viewModels()
    private val batchViewModel: BatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val fermentableId = data?.getStringExtra(Constants.EXTRA_ADD_GRAIN)
                if (fermentableId != null) {
                    ingredientViewModel.loadFermentableDetails(fermentableId)
                }

                val hopId = data?.getStringExtra(Constants.EXTRA_ADD_HOP)
                if (hopId != null) {
                    ingredientViewModel.loadHopDetails(hopId)
                }
            }
        }

        initializeRetryButton()

        val fermentableSearchRecyclerView = binding.root.findViewById<RecyclerView>(R.id.fermentableSearchRecyclerView)
        fermentableSearchRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        fermentableSearchRecyclerView.adapter = FermentableSearchListAdapter(emptyList()) {}

        val hopSearchRecyclerView = binding.root.findViewById<RecyclerView>(R.id.hopSearchRecyclerView)
        hopSearchRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        hopSearchRecyclerView.adapter = HopSearchListAdapter(emptyList()) {}

        AzureAuthHelper.isUserSignedIn(this) {
            val recipeRecyclerView =
                binding.root.findViewById<RecyclerView>(R.id.recipeRecyclerView)
            recipeRecyclerView.layoutManager =
                LinearLayoutManager(
                    this,
                    RecyclerView.VERTICAL,
                    false
                )

            initializeRecipeSearchFilterUi()
            initializeSortUi()

            val searchBtn = binding.root.findViewById<Button>(R.id.searchRecipeBtn)
            searchBtn.setOnClickListener {
                loadSavedRecipePreviews()
            }

            recipeViewModel.loadRecipePreviewsResponse.observe(this@MainActivity) {
                recipes = sortRecipes(it)
                toggleRecipeRecyclerViewVisibility(true)
                displaySavedRecipePreviews(recipes, binding)
            }
            loadSavedRecipePreviews()

            setupBatchesInProgress()

            recipeViewModel.loadRecipeDefaultSettingsResponse.observe(this@MainActivity) {
                loadSettings(it)
            }
            recipeViewModel.loadRecipeDefaultSettings()

            ingredientViewModel.loadFermentableDetailsResponse.observe(this@MainActivity) {
                addFermentableToSearchCriteria(it)
            }

            ingredientViewModel.loadHopDetailsResponse.observe(this@MainActivity) {
                addHopToSearchCriteria(it)
            }
        }
    }

    private fun sortRecipes(recipes: List<RecipePreview>?): List<RecipePreview> {
        val sortTypeDisplayText = binding.root.findViewById<Spinner>(R.id.sortItemsSpinner).selectedItem.toString()
        val sortType = RecipeSortType.entries.find { it.displayText() == sortTypeDisplayText } ?: RecipeSortType.None

        val ascending = binding.root.findViewById<RadioButton>(R.id.radioAscending).isChecked

        return sortRecipes(recipes ?: emptyList(), sortType, ascending)
    }

    private fun initializeSortUi() {
        val sortSpinner = binding.root.findViewById<Spinner>(R.id.sortItemsSpinner)
        val sortValues = RecipeSortType.entries.map { it.displayText() }.toList()
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, sortValues)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        sortSpinner.adapter = adapter
        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                displaySavedRecipePreviews(sortRecipes(recipes), binding)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                displaySavedRecipePreviews(sortRecipes(recipes), binding)
            }
        }

        val ascendingRadioButtonGroup = binding.root.findViewById<RadioGroup>(R.id.radioGroupSortOrder)
        ascendingRadioButtonGroup.setOnCheckedChangeListener { _, _ ->
            displaySavedRecipePreviews(sortRecipes(recipes), binding)
        }
    }

    private fun initializeRetryButton() {
        val retryButton = findViewById<Button>(R.id.btnRetry)
        retryButton.visibility = View.INVISIBLE
        retryButton.setOnClickListener {
            loadSavedRecipePreviews()
            retryButton.visibility = View.INVISIBLE
        }
    }

    override fun onPause() {
        super.onPause()

        val recipeSearchFilter = getRecipeSearchFilter()
        RecipeSearchFilterUtility.saveRecipeSearchFilter(recipeSearchFilter, this)
    }

    private fun addFermentableToSearchCriteria(fermentable: Fermentable) {
        fermentablesToSearch.add(fermentable)
        setFermentableToSearchRecyclerView(fermentablesToSearch)
    }

    private fun setFermentableToSearchRecyclerView(fermentableList: List<Fermentable>) {
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.fermentableSearchRecyclerView)
        recyclerView.adapter = FermentableSearchListAdapter(fermentableList) {
            fermentable -> removeFermentableFromSearch(fermentable) }
    }

    private fun removeFermentableFromSearch(fermentable: Fermentable) {
        fermentablesToSearch.remove(fermentable)
        setFermentableToSearchRecyclerView(fermentablesToSearch)
    }

    private fun addHopToSearchCriteria(hop: Hop) {
        hopsToSearch.add(hop)
        setHopToSearchRecyclerView(hopsToSearch)
    }

    private fun setHopToSearchRecyclerView(hopList: List<Hop>) {
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.hopSearchRecyclerView)
        recyclerView.adapter = HopSearchListAdapter(hopList) {
                hop -> removeHopFromSearch(hop) }
    }

    private fun removeHopFromSearch(hop: Hop) {
        hopsToSearch.remove(hop)
        setHopToSearchRecyclerView(hopsToSearch)
    }

    private fun showColorPickerDialog(colorCardView: CardView, colorTextView: TextView) {
        val recyclerView = RecyclerView(this)
        val colorPickerDialog = AlertDialog.Builder(this)
            .setTitle("Choose a color")
            .setView(recyclerView)
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .setNeutralButton("Clear") { _, _ -> showDeselectedColorFilter(colorTextView, colorCardView) }
            .create()

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.setPadding(5)
        recyclerView.adapter = ColorPaletteDialogListAdapter(srmColors, R.layout.color_item_layout) { selectedColor ->
            val selectedSrmColor = srmColors.find { it.srmColor == selectedColor }!!
            colorCardView.setCardBackgroundColor(selectedSrmColor.rbgColor)
            colorTextView.text = selectedSrmColor.srmColor.toString()
            colorPickerDialog.dismiss()
        }

        colorPickerDialog.show()
    }

    private fun initializeRecipeSearchFilterUi() {
        initializeExpandSearchButton(binding)
        initializeSearchButtons(binding)

        val recipeSearchFilter = RecipeSearchFilterUtility.loadRecipeSearchFilter(this)

        val abvCheckbox = findViewById<CheckBox>(R.id.chkAbvFilter)
        abvCheckbox.setOnCheckedChangeListener { _, isChecked: Boolean ->
            binding.root.findViewById<Spinner>(R.id.minAbvSpinner).isEnabled = isChecked
            binding.root.findViewById<Spinner>(R.id.maxAbvSpinner).isEnabled = isChecked
        }
        abvCheckbox.isChecked = recipeSearchFilter?.abvEnabled ?: false
        val minAbvIndex = if (recipeSearchFilter?.abvMin != null) abvValues.indexOf(recipeSearchFilter.abvMin) else 0
        initAbvSpinner(R.id.minAbvSpinner, minAbvIndex, binding.root, abvCheckbox.isChecked)
        val maxAbvIndex = if (recipeSearchFilter?.abvMax != null) abvValues.indexOf(recipeSearchFilter.abvMax) else abvValues.size - 1
        initAbvSpinner(R.id.maxAbvSpinner, maxAbvIndex, binding.root, abvCheckbox.isChecked)

        initColorFilter(R.id.selectedMinColorCardView, R.id.txtSelectedMinColor, recipeSearchFilter?.colorMin)
        initColorFilter(R.id.selectedMaxColorCardView, R.id.txtSelectedMaxColor, recipeSearchFilter?.colorMax)

        val aleCheckBox = findViewById<CheckBox>(R.id.chkAle)
        aleCheckBox.isChecked = recipeSearchFilter?.aleEnabled ?: false
        val lagerCheckBox = findViewById<CheckBox>(R.id.chkLager)
        lagerCheckBox.isChecked = recipeSearchFilter?.lagerEnabled ?: false

        if (!recipeSearchFilter?.fermentables.isNullOrEmpty()) {
            fermentablesToSearch.addAll(recipeSearchFilter!!.fermentables)
            setFermentableToSearchRecyclerView(fermentablesToSearch)
        }

        if (!recipeSearchFilter?.hops.isNullOrEmpty()) {
            hopsToSearch.addAll(recipeSearchFilter!!.hops)
            setHopToSearchRecyclerView(hopsToSearch)
        }

        if (!recipeSearchFilter?.recipeNameSearchTerm.isNullOrEmpty()) {
            val recipeNameSearchEditText = findViewById<TextView>(R.id.txtRecipeNameSearch)
            recipeNameSearchEditText.text = recipeSearchFilter!!.recipeNameSearchTerm
        }

        initDaysSinceLastUpdatedFilter(recipeSearchFilter)

        if (recipeSearchFilter?.searchFilterVisible == true) {
            findViewById<ConstraintLayout>(R.id.recipeSearchLayout).visibility = View.VISIBLE
            findViewById<ImageButton>(R.id.expandSearchBtn).setImageResource(R.drawable.baseline_expand_less_24)
        }
    }

    private fun initDaysSinceLastUpdatedFilter(recipeSearchFilter: RecipeSearchFilter?) {
        val daysSinceLastUpdatedSpinner = findViewById<Spinner>(R.id.daysUpdatedSpinner)

        val adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            LastUpdatedUtility.timePeriodsForDisplay()
        )
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        daysSinceLastUpdatedSpinner.adapter = adapter

        val daysSinceLastUpdatedIndex = LastUpdatedUtility.getLastUpdatedIndex(recipeSearchFilter?.daysSinceLastUpdated)
        daysSinceLastUpdatedSpinner.setSelection(daysSinceLastUpdatedIndex)
    }

    private fun initColorFilter(
        selectedColorCardViewId: Int, selectedColorTextId: Int,
        startingColor: Int?
    ) {
        val colorCardView = findViewById<CardView>(selectedColorCardViewId)
        val selectedColorTextView = findViewById<TextView>(selectedColorTextId)
        colorCardView.setOnClickListener {
            showColorPickerDialog(colorCardView, selectedColorTextView)
        }

        if (startingColor != null) {
            val color = srmColors.first { it.srmColor == startingColor }
            selectedColorTextView.text = color.srmColor.toString()
            colorCardView.setCardBackgroundColor(color.rbgColor)
        } else {
            showDeselectedColorFilter(selectedColorTextView, colorCardView)
        }
    }

    private fun showDeselectedColorFilter(
        selectedColorTextView: TextView,
        colorCardView: CardView
    ) {
        selectedColorTextView.text = "--"
        colorCardView.setCardBackgroundColor(Color.GRAY)
    }

    private fun initAbvSpinner(id: Int, startingIndex: Int, view: View, isEnabled: Boolean) {
        val spinner = view.findViewById<Spinner>(id)
        spinner.isEnabled = isEnabled
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

    private fun initializeSearchButtons(binding: ActivityMainBinding) {
        with (binding.root) {
            val newFermentableBtn = findViewById<Button>(R.id.btnNewFermentableSearch)
            newFermentableBtn.setOnClickListener { addFermentable() }

            val newHopBtn = findViewById<Button>(R.id.btnNewHopSearch)
            newHopBtn.setOnClickListener { addHop() }
        }
    }

    private fun loadSavedRecipePreviews() {
        toggleRecipeRecyclerViewVisibility(false)

        val recipeSearchFilter = getRecipeSearchFilter()
        recipeViewModel.loadRecipePreviews(recipeSearchFilter) {
            showRetryButton()
        }
    }

    private fun getRecipeSearchFilter() : RecipeSearchFilter {
        with(binding.root) {
            val abvCheckbox = findViewById<CheckBox>(R.id.chkAbvFilter)
            val minAbvSpinner = findViewById<Spinner>(R.id.minAbvSpinner)
            val abvMin = if (abvCheckbox.isChecked) abvValues[minAbvSpinner.selectedItemPosition] else null
            val maxAbvSpinner = findViewById<Spinner>(R.id.maxAbvSpinner)
            val abvMax = if (abvCheckbox.isChecked) abvValues[maxAbvSpinner.selectedItemPosition] else null

            val colorMin = findViewById<TextView>(R.id.txtSelectedMinColor).text.toString().toIntOrNull()
            val colorMax = findViewById<TextView>(R.id.txtSelectedMaxColor).text.toString().toIntOrNull()

            val aleChecked = findViewById<CheckBox>(R.id.chkAle).isChecked
            val lagerChecked = findViewById<CheckBox>(R.id.chkLager).isChecked
            val recipeType = if (aleChecked && !lagerChecked) "ale"
                else if (!aleChecked && lagerChecked) "lager"
                else null

            val lastUpdatedSpinner = findViewById<Spinner>(R.id.daysUpdatedSpinner)
            val daysSinceLastUpdated = LastUpdatedUtility.getDaysSinceUpdated(lastUpdatedSpinner.selectedItemPosition)

            val recipeNameSearchTerm = findViewById<TextView>(R.id.txtRecipeNameSearch).text.toString()

            val searchFilterVisible = findViewById<ConstraintLayout>(R.id.recipeSearchLayout).isVisible

            return RecipeSearchFilter(abvCheckbox.isChecked, abvMin, abvMax,
                colorMin, colorMax, aleChecked, lagerChecked, recipeType,
                fermentablesToSearch, hopsToSearch, daysSinceLastUpdated, recipeNameSearchTerm, searchFilterVisible)
        }
    }

    private fun displaySavedRecipePreviews(
        recipes: List<RecipePreview>,
        binding: ActivityMainBinding
    ) {
        with (binding.root) {
            val txtRecipeCount = findViewById<TextView>(com.jlafshari.beerrecipegenerator.R.id.txtRecipeCount)
            val recipeRecyclerView = findViewById<RecyclerView>(com.jlafshari.beerrecipegenerator.R.id.recipeRecyclerView)
            recipeRecyclerView.adapter =
                RecipeListAdapter(recipes) { recipePreview ->
                    recipePreviewClicked(recipePreview)
                }
            txtRecipeCount.text = context.getString(R.string.recipes_count, recipes.size.toString())
        }
    }

    private fun toggleRecipeRecyclerViewVisibility(showRecyclerView: Boolean) {
        hideRetryButton()
        with(binding.root) {
            val recipeRecyclerView = findViewById<RecyclerView>(R.id.recipeRecyclerView)
            val loadingIndicator = findViewById<CircularProgressIndicator>(R.id.loadingIndicator)

            if (showRecyclerView) {
                recipeRecyclerView.visibility = View.VISIBLE
                loadingIndicator.visibility = View.INVISIBLE
            } else {
                recipeRecyclerView.visibility = View.INVISIBLE
                loadingIndicator.visibility = View.VISIBLE
                loadingIndicator.bringToFront()
            }
        }
    }

    private fun showRetryButton() {
        val retryButton = findViewById<Button>(R.id.btnRetry)
        retryButton.visibility = View.VISIBLE

        val recipeRecyclerView = findViewById<RecyclerView>(R.id.recipeRecyclerView)
        recipeRecyclerView.visibility = View.INVISIBLE
        val loadingIndicator = findViewById<CircularProgressIndicator>(R.id.loadingIndicator)
        loadingIndicator.visibility = View.INVISIBLE
    }

    private fun hideRetryButton() {
        val retryButton = findViewById<Button>(R.id.btnRetry)
        retryButton.visibility = View.INVISIBLE
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

    fun newRecipe() {
        val newRecipeIntent = Intent(this, NewRecipeWizardActivity::class.java)
        startActivity(newRecipeIntent)
    }

    private fun recipePreviewClicked(recipePreview: RecipePreview) {
        val recipeViewIntent = Intent(this, RecipeViewActivity::class.java)
        recipeViewIntent.putExtra(Constants.EXTRA_VIEW_RECIPE, recipePreview.id)
        startActivity(recipeViewIntent)
    }

    private fun loadSettings(recipeDefaultSettings: RecipeDefaultSettings) {
        val settings = getSharedPreferences(AppSettings.PREFERENCE_FILE_NAME, MODE_PRIVATE)
        AppSettings.loadSettings(settings, recipeDefaultSettings)
    }
    
    private fun setupBatchesInProgress() {
        batchesInProgressAdapter = BatchesInProgressAdapter(::batchPreviewClicked)
        
        val batchesInProgressRecyclerView = binding.root.findViewById<RecyclerView>(R.id.rvBatchesInProgress)
        batchesInProgressRecyclerView.apply {
            adapter = batchesInProgressAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        
        val batchesInProgressSection = binding.root.findViewById<ConstraintLayout>(R.id.batchesInProgressSection)
        val txtNoBatches = binding.root.findViewById<TextView>(R.id.txtNoBatches)
        batchViewModel.batchesInProgressResponse.observe(this) { batches ->
            batches?.let {
                batchesInProgressSection.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
                txtNoBatches.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                batchesInProgressAdapter.updateBatches(it)
            }
        }
        
        batchViewModel.loadBatchesInProgress()
    }

    private fun batchPreviewClicked(batchId: String) {
        val viewBatchIntent = Intent(this, BatchViewActivity::class.java)
        viewBatchIntent.putExtra(Constants.EXTRA_VIEW_BATCH, batchId)
        startActivity(viewBatchIntent)
    }

    private fun addFermentable() {
        val addGrainIntent = Intent(this, AddGrainActivity::class.java)
        addGrainIntent.putExtra(Constants.EXTRA_ADD_GRAIN_GRAINS_TO_EXCLUDE, fermentablesToSearch.map { it.id }.toTypedArray())
        resultLauncher.launch(addGrainIntent)
    }

    private fun addHop() {
        val addHopIntent = Intent(this, AddHopActivity::class.java)
        addHopIntent.putExtra(Constants.EXTRA_ADD_HOP_HOPS_TO_EXCLUDE, hopsToSearch.map { it.id }.toTypedArray())
        resultLauncher.launch(addHopIntent)
    }
}
