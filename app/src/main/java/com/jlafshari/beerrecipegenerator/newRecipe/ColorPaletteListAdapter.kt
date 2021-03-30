package com.jlafshari.beerrecipegenerator.newRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.srmColors.SrmColor

class ColorPaletteListAdapter(
    private val colorList: List<SrmColor>,
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
        val srmColor = colorList[position]
        holder.txtColorSrmValue.text = srmColor.srmColor.toString()

        holder.colorCardView.setCardBackgroundColor(srmColor.rbgColor)
        val cardWidth: Int = if (selectedPos == position) {
            630
        } else {
            600
        }
        val cardHeight: Int = if (selectedPos == position) {
            170
        } else {
            140
        }
        holder.colorCardView.layoutParams = LinearLayout.LayoutParams(cardWidth, cardHeight)

        holder.itemView.setOnClickListener {
            val previousItem = selectedPos
            selectedPos = position
            notifyItemChanged(position)
            notifyItemChanged(previousItem)

            clickListener(srmColor.srmColor)
        }
    }

    override fun getItemCount() = colorList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtColorSrmValue: TextView = itemView.findViewById(R.id.colorSrmNumberTextView)
        val colorCardView: CardView = itemView.findViewById(R.id.colorCardView)
    }
}