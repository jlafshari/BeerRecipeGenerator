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
    private lateinit var mIbuValues: List<Int>
    private lateinit var mRecipeIbuPicker: NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bitterness, container, false)
        mRecipeIbuPicker = view.findViewById(R.id.ibuPicker)

        mIbuValues = getIbuValues(mCallback.getBitternessThreshold())
        val startingIbuIndex = setPickerToMedianValue()

        mCallback.onBitternessValueSet(mIbuValues[startingIbuIndex])

        return view
    }

    override fun onResume() {
        mIbuValues = getIbuValues(mCallback.getBitternessThreshold())
        val recipeGenerationInfo = mCallback.getCurrentRecipeGenerationInfo()
        if (recipeGenerationInfo.ibu != null) {
            val index = mIbuValues.indexOf(recipeGenerationInfo.ibu)
            setUpRecipeIbuPicker(mIbuValues.map { it.toString() }, index)
        } else {
            setPickerToMedianValue()
        }
        super.onResume()
    }

    private fun setPickerToMedianValue(): Int {
        val startingIbuIndex = getMedianIbuIndex(mIbuValues)
        setUpRecipeIbuPicker(mIbuValues.map { it.toString() }, startingIbuIndex)
        return startingIbuIndex
    }

    private fun setUpRecipeIbuPicker(ibuValues: List<String>, startingIbuIndex: Int) {
        mRecipeIbuPicker.displayedValues = null
        mRecipeIbuPicker.minValue = 0
        mRecipeIbuPicker.maxValue = ibuValues.size - 1
        mRecipeIbuPicker.value = startingIbuIndex
        mRecipeIbuPicker.displayedValues = ibuValues.toTypedArray()
        mRecipeIbuPicker.wrapSelectorWheel = false

        mRecipeIbuPicker.setOnValueChangedListener { _, _, newVal ->
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

    interface BitternessCallback : RecipeInfoListener {
        fun onBitternessValueSet(bitterness: Int?)
        fun getBitternessThreshold(): StyleThreshold
    }
}