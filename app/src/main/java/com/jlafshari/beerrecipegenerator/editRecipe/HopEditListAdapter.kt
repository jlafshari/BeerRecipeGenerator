package com.jlafshari.beerrecipegenerator.editRecipe

import android.text.Editable
import android.text.TextWatcher
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
     private val amountChangedListener: (amount: Double, index: Int) -> Unit,
     private val hopBoilAdditionTimeChangedListener: (boilAdditionTime: Int, index: Int) -> Unit,
     private val deleteHopListener: (index: Int) -> Unit) :
    RecyclerView.Adapter<HopEditListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.hop_edit_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hop = hopList[holder.adapterPosition]
        holder.txtHopAmount.text.insert(0, hop.amount.toString())
        holder.txtHopAdditionTime.text.insert(0, hop.boilAdditionTime.toString())
        holder.txtHopAdditionTime.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(hopBoilAdditionTimeEditText: Editable?) {
                val boilAdditionTime = hopBoilAdditionTimeEditText.toString().toIntOrNull()
                if (boilAdditionTime != null)
                    hopBoilAdditionTimeChangedListener(boilAdditionTime, holder.adapterPosition)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        holder.txtHop.text = hop.name
        holder.txtHopAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(hopAmountEditText: Editable?) {
                val amount = hopAmountEditText.toString().toDoubleOrNull()
                if (amount != null) {
                    amountChangedListener(amount, holder.adapterPosition)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        holder.btnDeleteHop.setOnClickListener { deleteHopListener(holder.adapterPosition) }
    }

    override fun getItemCount() = hopList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtHopAmount: EditText = itemView.findViewById(R.id.txtHopAmountEdit)
        val txtHop: TextView = itemView.findViewById(R.id.txtHopEdit)
        val txtHopAdditionTime: EditText = itemView.findViewById(R.id.txtHopAdditionTimeEdit)
        val btnDeleteHop: Button = itemView.findViewById(R.id.btnDeleteHop)
    }
}