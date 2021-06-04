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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Style
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.ui.login.AuthHelper

class BeerStyleFragment : Fragment() {
    private var mCallback: OnRecipeStyleSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_beer_style, container, false)

        val styleSpinner = view.findViewById<Spinner>(R.id.styleSpinner)
        loadRecipeStyles(styleSpinner)

        styleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mCallback?.onRecipeStyleSelected(parent?.getItemAtPosition(position) as Style)
            }
        }

        return view
    }

    private fun loadRecipeStyles(styleSpinner: Spinner) {
        val queue = Volley.newRequestQueue(this.context)
        val stringRequest = object : StringRequest(
            Method.GET, resources.getString(R.string.getAllStylesUrl),
            {
                val json = jacksonObjectMapper()
                val recipeStyles: List<Style> = json.readValue(it)
                val adapter = ArrayAdapter(requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    recipeStyles)
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                styleSpinner.adapter = adapter
            },
            {
                println(it)
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val accessToken = AuthHelper.sessionClient?.tokens?.accessToken
                headers["Authorization"] = "Bearer $accessToken"
                return headers
            }
        }
        queue.add(stringRequest)
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
    }
}