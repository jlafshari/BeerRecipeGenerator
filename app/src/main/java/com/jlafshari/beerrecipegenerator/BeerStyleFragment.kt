package com.jlafshari.beerrecipegenerator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jlafshari.beerrecipecore.Style
import java.nio.charset.Charset

class BeerStyleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_beer_style, container, false)

        val json = jacksonObjectMapper()
        val recipeStylesJSON = resources.openRawResource(R.raw.recipe_styles).readBytes().toString(Charset.defaultCharset())
        val recipeStyles: List<Style> = json.readValue(recipeStylesJSON)

        val adapter = ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item,
            recipeStyles.map { it.name })
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        val styleSpinner = view.findViewById<Spinner>(R.id.styleSpinner)
        styleSpinner.adapter = adapter

        return view
    }
}