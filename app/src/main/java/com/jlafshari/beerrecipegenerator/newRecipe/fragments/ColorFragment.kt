package com.jlafshari.beerrecipegenerator.newRecipe.fragments

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
import com.jlafshari.beerrecipegenerator.newRecipe.ColorPaletteListAdapter
import com.jlafshari.beerrecipegenerator.newRecipe.RecipeInfoListener
import com.jlafshari.beerrecipegenerator.srmColors.Colors

class ColorFragment : Fragment() {
    private lateinit var mCallback: ColorCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_color, container, false)

        val colorRecyclerView = view.findViewById<RecyclerView>(R.id.colorPaletteRecyclerView)
        colorRecyclerView.layoutManager = LinearLayoutManager(
            this.context, RecyclerView.VERTICAL, false)

        loadColorPalette(colorRecyclerView)

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

    override fun onResume() {
        val colorRecyclerView = requireView().findViewById<RecyclerView>(R.id.colorPaletteRecyclerView)
        loadColorPalette(colorRecyclerView)
        super.onResume()
    }

    private fun loadColorPalette(colorRecyclerView: RecyclerView) {
        val srmColorThreshold = mCallback.getSrmColorThreshold()
        val colors = Colors.getColorsInRange(
            srmColorThreshold.minimum.toInt(),
            srmColorThreshold.maximum.toInt()
        )
        colorRecyclerView.adapter = ColorPaletteListAdapter(colors, R.layout.color_srm_item_layout)
            { colorValueSrm -> mCallback.onColorValueSet(colorValueSrm) }

        val recipeGenerationInfo = mCallback.getCurrentRecipeGenerationInfo()
        if (recipeGenerationInfo.colorSrm != null) {
            val startingPosition = colors.indexOfFirst { it.srmColor == recipeGenerationInfo.colorSrm }
            colorRecyclerView.postDelayed({
                colorRecyclerView.findViewHolderForAdapterPosition(startingPosition)?.itemView?.performClick()
            }, 10)
        }
    }

    interface ColorCallback: RecipeInfoListener {
        fun onColorValueSet(colorSrm: Int?)
        fun getSrmColorThreshold(): StyleThreshold
    }
}
