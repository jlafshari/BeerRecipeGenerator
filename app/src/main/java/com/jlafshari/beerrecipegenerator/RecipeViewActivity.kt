package com.jlafshari.beerrecipegenerator

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun deleteRecipe() {
        MyRecipesHelper.deleteRecipe(mRecipe!!.id, getExternalFilesDir(null)!!)
        Toast.makeText(this, "Recipe deleted!", Toast.LENGTH_SHORT).show()

        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
    }
}