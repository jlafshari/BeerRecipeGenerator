package com.jlafshari.beerrecipegenerator.newRecipe

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipegenerator.R

class ColorPaletteListAdapter(
    private val context: Context,
    private val colorList: List<Int>,
    private val clickListener: (Int) -> Unit
):
    RecyclerView.Adapter<ColorPaletteListAdapter.ViewHolder>() {
    private var selectedPos = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.color_srm_item_layout,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colorSrmValue = colorList[position]
        holder.txtColorSrmValue.text = colorSrmValue.toString()

        holder.colorCardView.setCardBackgroundColor(Color.TRANSPARENT)
        if (selectedPos == position)
            holder.colorCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))

        holder.itemView.setOnClickListener {
            val previousItem = selectedPos
            selectedPos = position
            notifyItemChanged(position)
            notifyItemChanged(previousItem)

            clickListener(colorSrmValue)
        }
    }

    override fun getItemCount() = colorList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtColorSrmValue: TextView = itemView.findViewById(R.id.colorSrmNumberTextView)
        val colorCardView: CardView = itemView.findViewById(R.id.colorCardView)
    }
}