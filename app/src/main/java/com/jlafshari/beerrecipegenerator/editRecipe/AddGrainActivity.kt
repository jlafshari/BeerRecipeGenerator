package com.jlafshari.beerrecipegenerator.editRecipe

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Fermentable
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.HomebrewApiRequestHelper
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.databinding.ActivityAddGrainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddGrainActivity : AppCompatActivity() {
    private lateinit var mFermentables: List<Fermentable>
    private lateinit var mBinding: ActivityAddGrainBinding

    @Inject
    lateinit var requestHelper: HomebrewApiRequestHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddGrainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.txtGrainSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = searchGrains()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        mBinding.grainSelectorRecyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        setGrainSelectorView(emptyList())

        val grainsToExclude = intent.getStringArrayExtra(Constants.EXTRA_ADD_GRAIN_GRAINS_TO_EXCLUDE)!!
        loadFermentables(grainsToExclude)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_grain, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_cancel_add_grain) {
            val editRecipeIntent = Intent(this, EditRecipeActivity::class.java)
            startActivity(editRecipeIntent)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadFermentables(grainsToExclude: Array<String>) {
        val callBack = requestHelper.getVolleyCallBack(this@AddGrainActivity) { run {
            mFermentables = jacksonObjectMapper().readValue<List<Fermentable>>(it)
                .filter { g -> !grainsToExclude.contains(g.id) }
            setGrainSelectorView(mFermentables)
        }}
        requestHelper.getAllFermentables(this, callBack)
    }

    private fun setGrainSelectorView(grainList: List<Fermentable>) {
        mBinding.grainSelectorRecyclerView.adapter = GrainSelectorListAdapter(grainList) {
                fermentable -> grainClicked(fermentable) }
    }

    private fun grainClicked(fermentable: Fermentable) {
        val editRecipeIntent = Intent(this, EditRecipeActivity::class.java)
        editRecipeIntent.putExtra(Constants.EXTRA_ADD_GRAIN, fermentable.id)
        startActivity(editRecipeIntent)
    }

    fun searchGrains() {
        if (!this::mFermentables.isInitialized) return

        val searchText = mBinding.txtGrainSearch.text.toString()
        val grainsMatched = mFermentables.filter { it.name.contains(searchText, true) }
        setGrainSelectorView(grainsMatched)
    }
}