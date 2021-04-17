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
import com.jlafshari.beerrecipegenerator.R

class AbvFragment : Fragment() {
    private var mCallback: AbvCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_abv, container, false)

        val recipeAbvPicker = view.findViewById<NumberPicker>(R.id.abvPicker)
        val abvValues = getAbvValues(mCallback?.getAbvThreshold()!!)
        recipeAbvPicker.minValue = 0
        recipeAbvPicker.maxValue = abvValues.size - 1
        recipeAbvPicker.value = 2
        recipeAbvPicker.displayedValues = abvValues.toTypedArray()
        recipeAbvPicker.wrapSelectorWheel = false

        recipeAbvPicker.setOnValueChangedListener { _, _, newVal ->
            mCallback?.onAbvValueSet(abvValues[newVal].toDoubleOrNull())
        }
        mCallback?.onAbvValueSet(abvValues[2].toDoubleOrNull())

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AbvCallback) {
            mCallback = context
        } else {
            throw ClassCastException("$context must implement ${AbvCallback::class.simpleName}")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    interface AbvCallback {
        fun onAbvValueSet(abv: Double?)
        fun getAbvThreshold(): StyleThreshold
    }
}