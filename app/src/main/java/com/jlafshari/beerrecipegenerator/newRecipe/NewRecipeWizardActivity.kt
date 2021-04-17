package com.jlafshari.beerrecipegenerator.newRecipe

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.jlafshari.beerrecipecore.Recipe
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.Style
import com.jlafshari.beerrecipecore.StyleThreshold
import com.jlafshari.beerrecipegenerator.MainActivity
import com.jlafshari.beerrecipegenerator.MyRecipesHelper
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.databinding.ActivityNewRecipeWizardBinding
import com.jlafshari.beerrecipegenerator.newRecipe.AbvFragment.AbvCallback
import com.jlafshari.beerrecipegenerator.newRecipe.BeerStyleFragment.OnRecipeStyleSelectedListener
import com.jlafshari.beerrecipegenerator.newRecipe.ColorFragment.ColorCallback
import com.jlafshari.beerrecipegenerator.newRecipe.RecipeSizeFragment.OnRecipeSizeSetListener
import com.jlafshari.beerrecipegenerator.newRecipe.SaveRecipeFragment.OnSaveRecipeListener
import java.util.*

class NewRecipeWizardActivity : AppCompatActivity(), OnRecipeStyleSelectedListener,
    OnRecipeSizeSetListener, AbvCallback, ColorCallback, OnSaveRecipeListener {

    private val mRecipeGenerationInfo: RecipeGenerationInfo = RecipeGenerationInfo()

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

    private fun cancelNewRecipe() {
        goToMainActivity()
    }

    private fun goToMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
    }

    override fun onRecipeStyleSelected(style: Style) {
        mRecipeGenerationInfo.style = style
    }

    override fun onRecipeSizeSet(recipeSize: Double?) {
        mRecipeGenerationInfo.size = recipeSize
    }

    override fun onAbvValueSet(abv: Double?) {
        mRecipeGenerationInfo.abv = abv
    }

    override fun getAbvThreshold(): StyleThreshold =
        mRecipeGenerationInfo.style!!.abvThreshold

    override fun onColorValueSet(colorSrm: Int?) {
        mRecipeGenerationInfo.colorSrm = colorSrm
    }

    override fun getSrmColorThreshold(): StyleThreshold =
        mRecipeGenerationInfo.style!!.colorThreshold

    override fun onSaveRecipe(recipeName: String) {
        val newRecipeId = UUID.randomUUID().toString()
        val recipe = Recipe(newRecipeId, mRecipeGenerationInfo.style!!, mRecipeGenerationInfo.size!!,
            recipeName, mRecipeGenerationInfo.abv!!, mRecipeGenerationInfo.colorSrm!!)
        MyRecipesHelper.saveRecipe(recipe, getExternalFilesDir(null)!!)

        Toast.makeText(this, "Saved recipe!", Toast.LENGTH_SHORT).show()

        goToMainActivity()
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
