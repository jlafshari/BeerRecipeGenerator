package com.jlafshari.beerrecipegenerator.newRecipe

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.StyleThreshold
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.srmColors.Colors

class ColorFragment : Fragment() {
    private var mCallback: ColorCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_color, container, false)

        val colorRecyclerView = view.findViewById<RecyclerView>(R.id.colorPaletteRecyclerView)
        colorRecyclerView.layoutManager = LinearLayoutManager(
            this.context, RecyclerView.VERTICAL, false)

        val srmColorThreshold = mCallback!!.getSrmColorThreshold()
        val colors = Colors.getColorsInRange(srmColorThreshold.minimum.toInt(), srmColorThreshold.maximum.toInt())
        colorRecyclerView.adapter = ColorPaletteListAdapter(colors)
            { colorValueSrm -> mCallback?.onColorValueSet(colorValueSrm)}

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ColorCallback) {
            mCallback = context
        } else {
            throw ClassCastException("$context must implement ${ColorCallback::class.simpleName}")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    interface ColorCallback {
        fun onColorValueSet(colorSrm: Int?)
        fun getSrmColorThreshold(): StyleThreshold
    }
}
