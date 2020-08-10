package com.jlafshari.beerrecipegenerator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Style
import java.nio.charset.Charset

class BeerStyleFragment : Fragment() {
    private var mCallback: OnRecipeStyleSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_beer_style, container, false)

        val json = jacksonObjectMapper()
        val recipeStylesJSON = resources.openRawResource(R.raw.recipe_styles).readBytes().toString(Charset.defaultCharset())
        val recipeStyles: List<Style> = json.readValue(recipeStylesJSON)

        val adapter = ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item,
            recipeStyles)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        val styleSpinner = view.findViewById<Spinner>(R.id.styleSpinner)
        styleSpinner.adapter = adapter

        styleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mCallback?.onRecipeStyleSelected(parent?.getItemAtPosition(position) as Style)
            }
        }

        return view
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