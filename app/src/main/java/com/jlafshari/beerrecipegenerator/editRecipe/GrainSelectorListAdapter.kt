package com.jlafshari.beerrecipegenerator.editRecipe

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.Fermentable
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.srmColors.Colors

class GrainSelectorListAdapter(private val grainList: List<Fermentable>, private val clickListener: (Fermentable) -> Unit) :
    RecyclerView.Adapter<GrainSelectorListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.grain_select_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val grain = grainList[position]
        holder.txtGrainName.text = grain.name
        holder.txtGrainDescription.text = grain.notes
        val rbgColor = Colors.getColor(grain.color.toInt())
        if (rbgColor != null) {
            holder.colorCardView.setCardBackgroundColor(rbgColor.rbgColor)
        }
        else {
            holder.colorCardView.setCardBackgroundColor(Color.TRANSPARENT)
        }
        holder.txtMaltCategory.text = grain.maltCategory
        holder.bind(grain, clickListener)
    }

    override fun getItemCount() = grainList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtGrainName: TextView = itemView.findViewById(R.id.txtGrainSelectName)
        val txtGrainDescription: TextView = itemView.findViewById(R.id.txtGrainDescription)
        val colorCardView : CardView = itemView.findViewById(R.id.colorCardView)
        val txtMaltCategory : TextView = itemView.findViewById(R.id.txtMaltCategory)

        fun bind(fermentable: Fermentable, clickListener: (Fermentable) -> Unit) =
            itemView.setOnClickListener { clickListener(fermentable) }
    }
}