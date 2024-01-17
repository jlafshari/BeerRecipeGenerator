package com.jlafshari.beerrecipegenerator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipegenerator.srmColors.SrmColor

class ColorPaletteDialogListAdapter(
    private val colorList: List<SrmColor>,
    private val resource: Int,
    private val clickListener: (Int) -> Unit
):
    RecyclerView.Adapter<ColorPaletteDialogListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            resource,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val srmColor = colorList[holder.adapterPosition]
        holder.txtColorSrmValue.text = srmColor.srmColor.toString()
        holder.colorCardView.setCardBackgroundColor(srmColor.rbgColor)

        holder.itemView.setOnClickListener {
            clickListener(srmColor.srmColor)
        }
    }

    override fun getItemCount() = colorList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtColorSrmValue: TextView = itemView.findViewById(R.id.colorSrmNumberTextView)
        val colorCardView: CardView = itemView.findViewById(R.id.colorCardView)
    }
}