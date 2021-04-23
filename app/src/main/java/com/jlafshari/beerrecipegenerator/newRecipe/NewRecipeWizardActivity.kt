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
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Recipe
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.Style
import com.jlafshari.beerrecipecore.StyleThreshold
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.MainActivity
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.RecipeViewActivity
import com.jlafshari.beerrecipegenerator.databinding.ActivityNewRecipeWizardBinding
import com.jlafshari.beerrecipegenerator.newRecipe.AbvFragment.AbvCallback
import com.jlafshari.beerrecipegenerator.newRecipe.BeerStyleFragment.OnRecipeStyleSelectedListener
import com.jlafshari.beerrecipegenerator.newRecipe.ColorFragment.ColorCallback
import com.jlafshari.beerrecipegenerator.newRecipe.GenerateRecipeFragment.OnGenerateRecipeListener
import com.jlafshari.beerrecipegenerator.newRecipe.RecipeSizeFragment.OnRecipeSizeSetListener

class NewRecipeWizardActivity : AppCompatActivity(), OnRecipeStyleSelectedListener,
    OnRecipeSizeSetListener, AbvCallback, ColorCallback, OnGenerateRecipeListener {

    private val mRecipeGenerationInfo: RecipeGenerationInfo = RecipeGenerationInfo()
    private var mStyle: Style? = null

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
        mStyle = style
        mRecipeGenerationInfo.styleId = style.id
    }

    override fun onRecipeSizeSet(recipeSize: Double?) {
        mRecipeGenerationInfo.size = recipeSize
    }

    override fun onAbvValueSet(abv: Double?) {
        mRecipeGenerationInfo.abv = abv
    }

    override fun getAbvThreshold(): StyleThreshold =
        mStyle!!.abvThreshold

    override fun onColorValueSet(colorSrm: Int?) {
        mRecipeGenerationInfo.colorSrm = colorSrm
    }

    override fun getSrmColorThreshold(): StyleThreshold =
        mStyle!!.colorThreshold

    override fun onGenerateRecipe(recipeName: String) {
        mRecipeGenerationInfo.name = recipeName
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method.POST, resources.getString(R.string.generateRecipeUrl),
            {
                Toast.makeText(this, "Saved recipe!", Toast.LENGTH_SHORT).show()

                val recipe: Recipe = jacksonObjectMapper().readValue(it)

                val recipeViewIntent = Intent(this, RecipeViewActivity::class.java)
                recipeViewIntent.putExtra(Constants.EXTRA_VIEW_RECIPE, recipe.id)
                startActivity(recipeViewIntent)
            },
            { println(it) })
            {
                override fun getBodyContentType(): String = "application/json"

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val jacksonObjectMapper = jacksonObjectMapper()
                    return jacksonObjectMapper.writeValueAsBytes(mRecipeGenerationInfo)
                }
            }
        queue.add(stringRequest)
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
