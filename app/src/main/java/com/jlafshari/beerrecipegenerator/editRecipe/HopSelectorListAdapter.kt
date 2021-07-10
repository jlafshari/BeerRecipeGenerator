package com.jlafshari.beerrecipegenerator.editRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.Hop
import com.jlafshari.beerrecipegenerator.R

class HopSelectorListAdapter(private val hopList: List<Hop>,
                             private val clickListener: (Hop) -> Unit) :
    RecyclerView.Adapter<HopSelectorListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.hop_select_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hop = hopList[position]
        holder.txtHopName.text = hop.name
        holder.txtHopDescription.text = hop.notes
        holder.bind(hop, clickListener)
    }

    override fun getItemCount() = hopList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtHopName: TextView = itemView.findViewById(R.id.txtHopSelectName)
        val txtHopDescription: TextView = itemView.findViewById(R.id.txtHopDescription)

        fun bind(hop: Hop, clickListener: (Hop) -> Unit) {
            itemView.setOnClickListener { clickListener(hop) }
        }
    }
}
