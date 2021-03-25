package com.jlafshari.beerrecipegenerator.newRecipe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.jlafshari.beerrecipegenerator.R

class AbvFragment : Fragment() {
    private var mCallback: OnAbvValueSetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_abv, container, false)

        val recipeAbvPicker = view.findViewById<NumberPicker>(R.id.abvPicker)
        val values = arrayOf("4", "4.5", "5", "5.5", "6", "6.5", "7")
        recipeAbvPicker.minValue = 0
        recipeAbvPicker.maxValue = values.size - 1
        recipeAbvPicker.value = 2
        recipeAbvPicker.displayedValues = values
        recipeAbvPicker.wrapSelectorWheel = false

        recipeAbvPicker.setOnValueChangedListener { _, _, newVal ->
            mCallback?.onAbvValueSet(values[newVal].toDoubleOrNull())
        }
        mCallback?.onAbvValueSet(values[2].toDoubleOrNull())

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAbvValueSetListener) {
            mCallback = context
        } else {
            throw ClassCastException("$context must implement ${OnAbvValueSetListener::class.simpleName}")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    interface OnAbvValueSetListener {
        fun onAbvValueSet(abv: Double?)
    }
}