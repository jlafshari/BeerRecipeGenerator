package com.jlafshari.beerrecipegenerator.editRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.Hop
import com.jlafshari.beerrecipegenerator.R

class HopSelectorListAdapter(private val hopList: List<Hop>) :
    RecyclerView.Adapter<HopSelectorListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.hop_select_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hop = hopList[position]
        holder.txtHopName.text = hop.name
    }

    override fun getItemCount() = hopList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtHopName: TextView = itemView.findViewById(R.id.txtHopSelectName)
    }
}
