package com.jlafshari.beerrecipegenerator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.Fermentable

class FermentableSearchListAdapter(
    private val fermentableList: List<Fermentable>
) :
    RecyclerView.Adapter<FermentableSearchListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fermentable_search_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return fermentableList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fermentable = fermentableList[position]
        holder.txtFermentableSearch.text = fermentable.name
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtFermentableSearch: TextView = itemView.findViewById(R.id.txtFermentableSearch)
    }
}
