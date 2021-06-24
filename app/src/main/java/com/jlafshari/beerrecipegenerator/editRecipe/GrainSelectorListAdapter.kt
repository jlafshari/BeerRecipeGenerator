package com.jlafshari.beerrecipegenerator.editRecipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.Fermentable
import com.jlafshari.beerrecipegenerator.R

class GrainSelectorListAdapter(private val grainList: List<Fermentable>, private val context: Context) :
    RecyclerView.Adapter<GrainSelectorListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.grain_select_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val grain = grainList[position]
        holder.txtGrainName.text = grain.name
    }

    override fun getItemCount() = grainList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtGrainName: TextView = itemView.findViewById(R.id.txtGrainSelectName)
    }
}