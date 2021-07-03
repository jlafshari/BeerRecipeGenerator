package com.jlafshari.beerrecipegenerator.editRecipe

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Hop
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.HomebrewApiRequestHelper
import com.jlafshari.beerrecipegenerator.VolleyCallBack
import com.jlafshari.beerrecipegenerator.databinding.ActivityAddHopBinding
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper

class AddHopActivity : AppCompatActivity() {
    private lateinit var mHops: List<Hop>
    private lateinit var mBinding: ActivityAddHopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddHopBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.hopSelectorRecyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false)
        setHopSelectorView(emptyList())

        loadHops()
    }

    private fun loadHops() {
        HomebrewApiRequestHelper.getAllHops(this, object : VolleyCallBack {
            override fun onSuccess(json: String) {
                mHops = jacksonObjectMapper().readValue(json)
                setHopSelectorView(mHops)
            }

            override fun onUnauthorizedResponse() {
                AuthHelper.startLoginActivity(this@AddHopActivity)
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@AddHopActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setHopSelectorView(hopList: List<Hop>) {
        mBinding.hopSelectorRecyclerView.adapter = HopSelectorListAdapter(hopList) {
                hop -> hopClicked(hop) }
    }

    private fun hopClicked(hop: Hop) {
        val editRecipeIntent = Intent(this, EditRecipeActivity::class.java)
        editRecipeIntent.putExtra(Constants.EXTRA_ADD_HOP, hop.id)
        startActivity(editRecipeIntent)
    }
}