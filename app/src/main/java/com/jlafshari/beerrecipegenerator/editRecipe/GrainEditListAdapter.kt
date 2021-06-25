package com.jlafshari.beerrecipegenerator.editRecipe

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.FermentableIngredient
import com.jlafshari.beerrecipegenerator.R

class GrainEditListAdapter(private val grainList: List<FermentableIngredient>,
                           private val context: Context,
                           private val amountChangedListener: (amount: Double, fermentableId: String) -> Unit) :
    RecyclerView.Adapter<GrainEditListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.grain_edit_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val grain = grainList[position]
        holder.txtGrainAmount.text.insert(0, grain.amount.toString())
        holder.txtGrain.text = grain.name
        holder.txtGrainAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(grainAmountEditText: Editable?) {
                val amount = grainAmountEditText.toString().toDoubleOrNull()
                if (amount != null) {
                    amountChangedListener(amount, grain.fermentableId)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun getItemCount() = grainList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtGrainAmount: EditText = itemView.findViewById(R.id.txtGrainAmountEdit)
        val txtGrain: TextView = itemView.findViewById(R.id.txtGrainEdit)
    }
}