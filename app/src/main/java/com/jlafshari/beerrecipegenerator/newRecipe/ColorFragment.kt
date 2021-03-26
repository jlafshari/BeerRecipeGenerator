package com.jlafshari.beerrecipegenerator.newRecipe

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jlafshari.beerrecipegenerator.R

class ColorFragment : Fragment() {
    private var mCallback: OnColorValueSetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_color, container, false)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnColorValueSetListener) {
            mCallback = context
        } else {
            throw ClassCastException("$context must implement ${OnColorValueSetListener::class.simpleName}")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    interface OnColorValueSetListener {
        fun onColorValueSet(colorSrm: Double?)
    }
}
