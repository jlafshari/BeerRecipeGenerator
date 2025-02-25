package com.jlafshari.beerrecipegenerator.editRecipe

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.HopIngredient
import com.jlafshari.beerrecipecore.HopUse
import com.jlafshari.beerrecipecore.displayText
import com.jlafshari.beerrecipegenerator.R

class HopEditListAdapter(private val hopList: List<HopIngredient>,
     private val amountChangedListener: (amount: Double, index: Int) -> Unit,
     private val hopBoilAdditionTimeChangedListener: (boilAdditionTime: Int, index: Int) -> Unit,
     private val deleteHopListener: (index: Int) -> Unit) :
    RecyclerView.Adapter<HopEditListAdapter.ViewHolder>() {
    private val hopUses = HopUse.entries.map { it.displayText() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.hop_edit_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hop = hopList[holder.adapterPosition]
        holder.txtHopAmount.text.insert(0, hop.amount.toString())
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

        holder.hopUseSpinner.adapter = ArrayAdapter(holder.itemView.context,
            android.R.layout.simple_spinner_dropdown_item, hopUses)
        holder.hopUseSpinner.setSelection(HopUse.entries.indexOf(hop.use))
        holder.hopUseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                hop.use = HopUse.entries[position]
                setElementsForHopUse(hop, holder)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        holder.txtHopBoilAdditionTime.text.clear()
        holder.txtHopBoilAdditionTime.text.insert(0, hop.boilAdditionTime.toString())
        holder.txtHopBoilAdditionTime.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(hopBoilAdditionTimeEditText: Editable?) {
                val boilAdditionTime = hopBoilAdditionTimeEditText.toString().toIntOrNull()
                if (boilAdditionTime != null)
                    hopBoilAdditionTimeChangedListener(boilAdditionTime, holder.adapterPosition)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        holder.txtWhirlpoolAdditionTime.text.clear()
        holder.txtWhirlpoolAdditionTime.text.insert(0, hop.whirlpoolDuration.toString())

        holder.txtDryHopDayStartEdit.text.clear()
        holder.txtDryHopDayStartEdit.text.insert(0, hop.dryHopDayStart.toString())
        holder.txtDryHopDayEndEdit.text.clear()
        holder.txtDryHopDayEndEdit.text.insert(0, hop.dryHopDayEnd.toString())

        setElementsForHopUse(hop, holder)
    }

    override fun getItemCount() = hopList.size

    private fun setElementsForHopUse(hop: HopIngredient, holder: ViewHolder) {
        with(holder) {
            when (hop.use) {
                HopUse.Boil -> {
                    dryHopLayout.visibility = View.GONE
                    txtHopBoilAdditionTime.visibility = View.VISIBLE
                    txtWhirlpoolAdditionTime.visibility = View.GONE
                    txtHopAdditionTimeUnit.visibility = View.VISIBLE
                }
                HopUse.Whirlpool -> {
                    dryHopLayout.visibility = View.GONE
                    txtHopBoilAdditionTime.visibility = View.GONE
                    txtWhirlpoolAdditionTime.visibility = View.VISIBLE
                    txtHopAdditionTimeUnit.visibility = View.VISIBLE
                }
                else -> {
                    dryHopLayout.visibility = View.VISIBLE
                    txtHopBoilAdditionTime.visibility = View.GONE
                    txtWhirlpoolAdditionTime.visibility = View.GONE
                    txtHopAdditionTimeUnit.visibility = View.GONE
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtHopAmount: EditText = itemView.findViewById(R.id.txtHopAmountEdit)
        val txtHop: TextView = itemView.findViewById(R.id.txtHopEdit)
        val txtHopBoilAdditionTime: EditText = itemView.findViewById(R.id.txtHopBoilAdditionTimeEdit)
        val txtWhirlpoolAdditionTime: EditText = itemView.findViewById(R.id.txtWhirlpoolAdditionTimeEdit)
        val txtHopAdditionTimeUnit: TextView = itemView.findViewById(R.id.txtHopAdditionTimeUnit)
        val btnDeleteHop: ImageButton = itemView.findViewById(R.id.btnDeleteHop)
        val hopUseSpinner: Spinner = itemView.findViewById(R.id.hopUseSpinner)

        val dryHopLayout: LinearLayout = itemView.findViewById(R.id.dryHopLayout)
        val txtDryHopDayStartEdit: EditText = itemView.findViewById(R.id.txtDryHopDayStartEdit)
        val txtDryHopDayEndEdit: EditText = itemView.findViewById(R.id.txtDryHopDayEndEdit)
    }
}