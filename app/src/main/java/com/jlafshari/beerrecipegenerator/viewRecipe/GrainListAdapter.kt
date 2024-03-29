package com.jlafshari.beerrecipegenerator.viewRecipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.FermentableIngredient
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.srmColors.Colors

class GrainListAdapter(private val grainList: List<FermentableIngredient>, private val context: Context) :
    RecyclerView.Adapter<GrainListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val grain = grainList[position]
        val color = Colors.getColor(grain.color.toInt())
        if (color != null)
            holder.srmGrainColorCardView.setCardBackgroundColor(color.rbgColor)
        holder.txtGrain.text = context.getString(R.string.grainListItem, grain.amount.toString(), grain.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.grain_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun getItemCount() = grainList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtGrain: TextView = itemView.findViewById(R.id.txtGrain)
        val srmGrainColorCardView: CardView = itemView.findViewById(R.id.srmGrainColorCardView)
    }
}