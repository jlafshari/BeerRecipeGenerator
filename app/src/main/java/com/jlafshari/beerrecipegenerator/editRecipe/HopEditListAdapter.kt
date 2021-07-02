package com.jlafshari.beerrecipegenerator.editRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.HopIngredient
import com.jlafshari.beerrecipegenerator.R

class HopEditListAdapter(private val hopList: List<HopIngredient>,
                         private val deleteHopListener: (hopId: String) -> Unit) :
    RecyclerView.Adapter<HopEditListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.hop_edit_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hop = hopList[position]
        holder.txtHopAmount.text.insert(0, hop.amount.toString())
        holder.txtHopAdditionTime.text.insert(0, hop.boilAdditionTime.toString())
        holder.txtHop.text = hop.name
        holder.btnDeleteHop.setOnClickListener { deleteHopListener(hop.hopId) }
    }

    override fun getItemCount() = hopList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtHopAmount: EditText = itemView.findViewById(R.id.txtHopAmountEdit)
        val txtHop: TextView = itemView.findViewById(R.id.txtHopEdit)
        val txtHopAdditionTime: EditText = itemView.findViewById(R.id.txtHopAdditionTimeEdit)
        val btnDeleteHop: Button = itemView.findViewById(R.id.btnDeleteHop)
    }
}