package com.jlafshari.beerrecipegenerator.newRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.RecipePreview

class ColorPaletteListAdapter(private val colorList: List<Int>, private val clickListener: (Int) -> Unit):
    RecyclerView.Adapter<ColorPaletteListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.color_srm_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colorSrmValue = colorList[position]
        holder.txtColorSrmValue.text = colorSrmValue.toString()
        holder.bind(colorSrmValue, clickListener)
    }

    override fun getItemCount() = colorList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtColorSrmValue: TextView = itemView.findViewById(R.id.colorSrmNumberTextView)

        fun bind(colorSrmValue: Int, clickListener: (Int) -> Unit) {
            itemView.setOnClickListener { clickListener(colorSrmValue) }
        }
    }
}