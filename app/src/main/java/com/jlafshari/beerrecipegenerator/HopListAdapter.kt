package com.jlafshari.beerrecipegenerator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.HopIngredient

class HopListAdapter(private val hopList: List<HopIngredient>, private val context: Context) :
    RecyclerView.Adapter<HopListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.hop_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hop = hopList[position]
        holder.txtHop.text = context.getString(R.string.hopListItem, hop.amount.toString(), hop.name,
            hop.boilAdditionTime.toString())
    }

    override fun getItemCount() = hopList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtHop: TextView = itemView.findViewById(R.id.txtHop)
    }
}