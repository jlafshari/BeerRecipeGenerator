package com.jlafshari.beerrecipegenerator.editRecipe

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Fermentable
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.HomebrewApiRequestHelper
import com.jlafshari.beerrecipegenerator.VolleyCallBack
import com.jlafshari.beerrecipegenerator.databinding.ActivityAddGrainBinding
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper

class AddGrainActivity : AppCompatActivity() {
    private lateinit var mFermentables: List<Fermentable>
    private lateinit var mBinding: ActivityAddGrainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddGrainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.grainSelectorRecyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        setGrainSelectorView(emptyList())

        loadFermentables()
    }

    private fun loadFermentables() {
        HomebrewApiRequestHelper.getAllFermentables(this, object : VolleyCallBack {
            override fun onSuccess(json: String) {
                mFermentables = jacksonObjectMapper().readValue(json)
                setGrainSelectorView(mFermentables)
            }

            override fun onUnauthorizedResponse() {
                AuthHelper.startLoginActivity(this@AddGrainActivity)
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@AddGrainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
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
}