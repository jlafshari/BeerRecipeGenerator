package com.jlafshari.beerrecipegenerator.newRecipe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Style
import com.jlafshari.beerrecipegenerator.HomebrewApiRequestHelper
import com.jlafshari.beerrecipegenerator.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BeerStyleFragment : Fragment() {
    private lateinit var mCallback: OnRecipeStyleSelectedListener

    @Inject
    lateinit var requestHelper: HomebrewApiRequestHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_beer_style, container, false)

        val startingStyle = mCallback.getCurrentStyle()
        val styleSpinner = view.findViewById<Spinner>(R.id.styleSpinner)
        loadRecipeStyles(styleSpinner, startingStyle)

        styleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mCallback.onRecipeStyleSelected(parent?.getItemAtPosition(position) as Style)
            }
        }

        return view
    }

    private fun loadRecipeStyles(styleSpinner: Spinner, startingStyle: Style?) {
        val callBack = requestHelper.getVolleyCallBack(this.requireContext()) { run {
            val recipeStyles: List<Style> = jacksonObjectMapper().readValue(it)
            val adapter = ArrayAdapter(requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                recipeStyles)
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            styleSpinner.adapter = adapter
            val startingPosition =
                if (startingStyle != null) recipeStyles.indexOfFirst { it.id == startingStyle.id }
                else 0
            styleSpinner.setSelection(startingPosition)
        }}
        requestHelper.getAllStyles(this.requireContext(), callBack)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnRecipeStyleSelectedListener)
            mCallback = context
        else
            throw ClassCastException(context.toString())
    }

    interface OnRecipeStyleSelectedListener {
        fun onRecipeStyleSelected(style: Style)
        fun getCurrentStyle() : Style?
    }
}