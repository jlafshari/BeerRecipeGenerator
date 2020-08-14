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
import com.jlafshari.beerrecipegenerator.MainActivity
import com.jlafshari.beerrecipegenerator.MyRecipesHelper
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.newRecipe.BeerStyleFragment.OnRecipeStyleSelectedListener
import com.jlafshari.beerrecipegenerator.newRecipe.RecipeSizeFragment.OnRecipeSizeSetListener
import com.jlafshari.beerrecipegenerator.newRecipe.SaveRecipeFragment.OnSaveRecipeListener
import kotlinx.android.synthetic.main.activity_new_recipe_wizard.*
import java.util.*

class NewRecipeWizardActivity : AppCompatActivity(), OnRecipeStyleSelectedListener,
    OnRecipeSizeSetListener, OnSaveRecipeListener {

    private val mRecipeGenerationInfo: RecipeGenerationInfo = RecipeGenerationInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_recipe_wizard)

        setSupportActionBar(toolbar)
        // Set up the ViewPager with the sections adapter.
        viewPager.adapter = SectionsPagerAdapter(this)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                progressBar.progress = position + 1
                super.onPageSelected(position)
            }
        })

        progressBar.max = NewRecipeSteps.numberOfSteps
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

    override fun onSaveRecipe(recipeName: String) {
        val newRecipeId = UUID.randomUUID().toString()
        val recipe = Recipe(newRecipeId, mRecipeGenerationInfo.style!!, mRecipeGenerationInfo.size!!, recipeName)
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