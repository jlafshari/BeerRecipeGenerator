package com.jlafshari.beerrecipegenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jlafshari.beerrecipecore.Recipe
import kotlinx.android.synthetic.main.activity_recipe_view.*

class RecipeViewActivity : AppCompatActivity() {
    private var mRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_view)

        val recipeId = intent.getStringExtra(Constants.EXTRA_VIEW_RECIPE)
        mRecipe = MyRecipesHelper.getRecipe(recipeId!!, getExternalFilesDir(null)!!)
        if (mRecipe == null) {
            throw Error("Could not load recipe with id \"$recipeId\"")
        }
        txt_recipe_name.text = mRecipe!!.name
        txt_style.text = getString(R.string.recipe_view_style_name, mRecipe!!.style.name)
        txt_size.text = getString(R.string.recipe_view_size, mRecipe!!.size.toString())
    }
}