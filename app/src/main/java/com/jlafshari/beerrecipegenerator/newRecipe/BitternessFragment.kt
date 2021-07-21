package com.jlafshari.beerrecipegenerator.newRecipe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.jlafshari.beerrecipecore.StyleThreshold
import com.jlafshari.beerrecipecore.utility.BitternessUtility.getIbuValues
import com.jlafshari.beerrecipecore.utility.BitternessUtility.getMedianIbuIndex
import com.jlafshari.beerrecipegenerator.R

class BitternessFragment : Fragment() {
    private lateinit var mCallback: BitternessCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bitterness, container, false)

        val ibuValues = getIbuValues(mCallback.getBitternessThreshold())
        val startingIbuIndex = getMedianIbuIndex(ibuValues)
        val recipeIbuPicker = view.findViewById<NumberPicker>(R.id.ibuPicker)
        setUpRecipeIbuPicker(recipeIbuPicker, ibuValues.map { it.toString() }, startingIbuIndex)

        mCallback.onBitternessValueSet(ibuValues[startingIbuIndex])

        return view
    }

    private fun setUpRecipeIbuPicker(recipeIbuPicker: NumberPicker, ibuValues: List<String>, startingIbuIndex: Int) {
        recipeIbuPicker.minValue = 0
        recipeIbuPicker.maxValue = ibuValues.size - 1
        recipeIbuPicker.value = startingIbuIndex
        recipeIbuPicker.displayedValues = ibuValues.toTypedArray()
        recipeIbuPicker.wrapSelectorWheel = false

        recipeIbuPicker.setOnValueChangedListener { _, _, newVal ->
            mCallback.onBitternessValueSet(ibuValues[newVal].toIntOrNull())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BitternessCallback) {
            mCallback = context
        } else {
            throw ClassCastException("$context must implement ${BitternessCallback::class.simpleName}")
        }
    }

    interface BitternessCallback {
        fun onBitternessValueSet(bitterness: Int?)
        fun getBitternessThreshold(): StyleThreshold
    }
}