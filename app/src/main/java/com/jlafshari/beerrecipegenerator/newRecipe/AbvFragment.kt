package com.jlafshari.beerrecipegenerator.newRecipe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.jlafshari.beerrecipecore.StyleThreshold
import com.jlafshari.beerrecipecore.utility.AbvUtility.getAbvValues
import com.jlafshari.beerrecipecore.utility.AbvUtility.getMedianAbvIndex
import com.jlafshari.beerrecipegenerator.R

class AbvFragment : Fragment() {
    private lateinit var mCallback: AbvCallback
    private lateinit var mAbvValues: List<Double>
    private lateinit var mRecipeAbvPicker: NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_abv, container, false)
        mRecipeAbvPicker = view.findViewById(R.id.abvPicker)

        mAbvValues = getAbvValues(mCallback.getAbvThreshold())
        val startingAbvIndex = setPickerToMedianValue()

        mCallback.onAbvValueSet(mAbvValues[startingAbvIndex])

        return view
    }

    override fun onResume() {
        mAbvValues = getAbvValues(mCallback.getAbvThreshold())
        val recipeGenerationInfo = mCallback.getCurrentRecipeGenerationInfo()
        if (recipeGenerationInfo.abv != null) {
            val index = mAbvValues.indexOf(recipeGenerationInfo.abv)
            setUpRecipeAbvPicker(mAbvValues.map { it.toString() }, index)
        } else {
            setPickerToMedianValue()
        }
        super.onResume()
    }

    private fun setPickerToMedianValue(): Int {
        val startingAbvIndex = getMedianAbvIndex(mAbvValues)
        setUpRecipeAbvPicker(mAbvValues.map { it.toString() }, startingAbvIndex)
        return startingAbvIndex
    }

    private fun setUpRecipeAbvPicker(
        abvValues: List<String>,
        startingAbvIndex: Int
    ) {
        mRecipeAbvPicker.displayedValues = null
        mRecipeAbvPicker.minValue = 0
        mRecipeAbvPicker.maxValue = abvValues.size - 1
        mRecipeAbvPicker.value = startingAbvIndex
        mRecipeAbvPicker.displayedValues = abvValues.toTypedArray()
        mRecipeAbvPicker.wrapSelectorWheel = false

        mRecipeAbvPicker.setOnValueChangedListener { _, _, newVal ->
            mCallback.onAbvValueSet(abvValues[newVal].toDoubleOrNull())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AbvCallback) {
            mCallback = context
        } else {
            throw ClassCastException("$context must implement ${AbvCallback::class.simpleName}")
        }
    }

    interface AbvCallback : RecipeInfoListener {
        fun onAbvValueSet(abv: Double?)
        fun getAbvThreshold(): StyleThreshold
    }
}