package com.jlafshari.beerrecipegenerator

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jlafshari.beerrecipecore.RecipeGenerationInfo
import com.jlafshari.beerrecipecore.Style
import com.jlafshari.beerrecipegenerator.BeerStyleFragment.OnRecipeStyleSelectedListener
import com.jlafshari.beerrecipegenerator.RecipeSizeFragment.OnRecipeSizeSetListener
import kotlinx.android.synthetic.main.activity_new_recipe_wizard.*

class NewRecipeWizardActivity : AppCompatActivity(), OnRecipeStyleSelectedListener,
    OnRecipeSizeSetListener {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    private val mRecipeGenerationInfo: RecipeGenerationInfo = RecipeGenerationInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_recipe_wizard)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(this)

        // Set up the ViewPager with the sections adapter.
        viewPager.adapter = mSectionsPagerAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = NewRecipeTabs.getTabTitle(position)
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_new_recipe_wizard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRecipeStyleSelected(style: Style) {
        mRecipeGenerationInfo.style = style
    }

    override fun onRecipeSizeSet(recipeSize: Double?) {
        mRecipeGenerationInfo.size = recipeSize
    }

    /**
     * A [FragmentStateAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount() = NewRecipeTabs.tabTitles.size

        override fun createFragment(position: Int): Fragment = NewRecipeTabs.getTabFragment(position)
    }
}
