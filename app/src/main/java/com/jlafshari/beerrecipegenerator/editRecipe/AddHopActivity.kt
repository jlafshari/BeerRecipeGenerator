package com.jlafshari.beerrecipegenerator.editRecipe

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Hop
import com.jlafshari.beerrecipegenerator.Constants
import com.jlafshari.beerrecipegenerator.HomebrewApiRequestHelper
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.VolleyCallBack
import com.jlafshari.beerrecipegenerator.databinding.ActivityAddHopBinding
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddHopActivity : AppCompatActivity() {
    private lateinit var mHops: List<Hop>
    private lateinit var mBinding: ActivityAddHopBinding

    @Inject
    lateinit var requestHelper: HomebrewApiRequestHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddHopBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.txtHopSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = searchHops()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        mBinding.hopSelectorRecyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false)
        setHopSelectorView(emptyList())

        loadHops()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_hop, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_cancel_add_hop) {
            val editRecipeIntent = Intent(this, EditRecipeActivity::class.java)
            startActivity(editRecipeIntent)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadHops() {
        requestHelper.getAllHops(this, object : VolleyCallBack {
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

    private fun searchHops() {
        if (!this::mHops.isInitialized) return

        val searchText = mBinding.txtHopSearch.text.toString()
        val hopsMatched = mHops.filter { it.name.contains(searchText, true) }
        setHopSelectorView(hopsMatched)
    }
}