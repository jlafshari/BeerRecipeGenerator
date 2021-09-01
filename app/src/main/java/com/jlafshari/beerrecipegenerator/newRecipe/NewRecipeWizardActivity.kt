package com.jlafshari.beerrecipegenerator.newRecipe

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Recipe
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.Style
import com.jlafshari.beerrecipecore.StyleThreshold
import com.jlafshari.beerrecipegenerator.*
import com.jlafshari.beerrecipegenerator.databinding.ActivityNewRecipeWizardBinding
import com.jlafshari.beerrecipegenerator.newRecipe.fragments.AbvFragment.AbvCallback
import com.jlafshari.beerrecipegenerator.newRecipe.fragments.BeerStyleFragment.OnRecipeStyleSelectedListener
import com.jlafshari.beerrecipegenerator.newRecipe.fragments.BitternessFragment.BitternessCallback
import com.jlafshari.beerrecipegenerator.newRecipe.fragments.ColorFragment.ColorCallback
import com.jlafshari.beerrecipegenerator.newRecipe.fragments.GenerateRecipeFragment.OnGenerateRecipeCallback
import com.jlafshari.beerrecipegenerator.newRecipe.fragments.RecipeSizeFragment.OnRecipeSizeSetListener
import com.jlafshari.beerrecipegenerator.recipes.RecipeValidationResult
import com.jlafshari.beerrecipegenerator.recipes.RecipeValidator
import com.jlafshari.beerrecipegenerator.settings.AppSettings
import com.jlafshari.beerrecipegenerator.viewRecipe.RecipeViewActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewRecipeWizardActivity : AppCompatActivity(), OnRecipeStyleSelectedListener,
    OnRecipeSizeSetListener, AbvCallback, ColorCallback, BitternessCallback, OnGenerateRecipeCallback {

    private var mRecipeGenerationInfo: RecipeGenerationInfo = RecipeGenerationInfo()
    private lateinit var mStyle: Style

    @Inject
    lateinit var requestHelper: HomebrewApiRequestHelper
    @Inject
    lateinit var recipeValidator: RecipeValidator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewRecipeWizardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        // Set up the ViewPager with the sections adapter.
        binding.viewPager.adapter = SectionsPagerAdapter(this)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.progressBar.progress = position + 1
                super.onPageSelected(position)
            }
        })

        binding.progressBar.max = NewRecipeSteps.numberOfSteps
        mRecipeGenerationInfo.extractionEfficiency = AppSettings.recipeDefaultSettings.extractionEfficiency
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_new_recipe_wizard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        if (id == R.id.action_cancel) {
            cancelNewRecipe()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun cancelNewRecipe() = goToMainActivity()

    private fun goToMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
    }

    override fun onRecipeStyleSelected(style: Style) {
        mStyle = style
        mRecipeGenerationInfo = RecipeGenerationInfo()
        mRecipeGenerationInfo.extractionEfficiency = AppSettings.recipeDefaultSettings.extractionEfficiency
        mRecipeGenerationInfo.styleId = style.id
    }

    override fun getCurrentStyle(): Style? {
        if (this::mStyle.isInitialized) return mStyle
        return null
    }

    override fun onRecipeSizeSet(recipeSize: Double?) {
        mRecipeGenerationInfo.size = recipeSize
    }

    override fun onAbvValueSet(abv: Double?) {
        mRecipeGenerationInfo.abv = abv
    }

    override fun getAbvThreshold(): StyleThreshold = mStyle.abvThreshold

    override fun onColorValueSet(colorSrm: Int?) {
        mRecipeGenerationInfo.colorSrm = colorSrm
    }

    override fun getSrmColorThreshold(): StyleThreshold = mStyle.colorThreshold

    override fun onBitternessValueSet(bitterness: Int?) {
        mRecipeGenerationInfo.ibu = bitterness
    }

    override fun getBitternessThreshold(): StyleThreshold = mStyle.ibuThreshold

    override fun onRecipeNameSet(recipeName: String) {
        mRecipeGenerationInfo.name = recipeName
    }

    override fun onGenerateRecipe(): RecipeValidationResult {
        val validationResult = recipeValidator.validateRecipeGenerationInfo(mRecipeGenerationInfo)

        if (validationResult.succeeded) {
            val callBack = requestHelper.getVolleyCallBack(this) { run {
                val recipe = jacksonObjectMapper().readValue<Recipe>(it)
                viewRecipe(recipe.id)
            }}
            requestHelper.generateRecipe(mRecipeGenerationInfo, this, callBack)
        }

        return validationResult
    }

    override fun getCurrentRecipeGenerationInfo(): RecipeGenerationInfo = mRecipeGenerationInfo

    private fun viewRecipe(recipeId: String) {
        val recipeViewIntent = Intent(this, RecipeViewActivity::class.java)
        recipeViewIntent.putExtra(Constants.EXTRA_VIEW_RECIPE, recipeId)
        startActivity(recipeViewIntent)
    }

    /**
     * A [FragmentStateAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount() = NewRecipeSteps.numberOfSteps

        override fun createFragment(position: Int): Fragment = NewRecipeSteps.getTabFragment(position)
    }
}
