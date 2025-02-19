package com.jlafshari.beerrecipegenerator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.Hop

class HopSearchListAdapter(
    private val hopList: List<Hop>,
    private val clickListener: (Hop) -> Unit
) :
    RecyclerView.Adapter<HopSearchListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.hop_search_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun getItemCount() = hopList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hop = hopList[position]
        holder.txtHopSearch.text = hop.name

        val drawable = holder.btnDeleteHopSearch.compoundDrawablesRelative[0]
        drawable?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(holder.itemView.context, R.color.colorPrimaryDark))
        }

        holder.bind(hop, clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtHopSearch: TextView = itemView.findViewById(R.id.txtHopSearch)
        val btnDeleteHopSearch: Button = itemView.findViewById(R.id.btnDeleteHopSearch)

        fun bind(hop: Hop, clickListener: (Hop) -> Unit) =
            btnDeleteHopSearch.setOnClickListener { clickListener(hop) }
    }
}