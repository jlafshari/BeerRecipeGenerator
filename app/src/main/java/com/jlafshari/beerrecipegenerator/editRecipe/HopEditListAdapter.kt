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
                         private val onAmountChanged: (amount: Double, index: Int) -> Unit,
                         private val onBoilAdditionTimeChanged: (boilAdditionTime: Int, index: Int) -> Unit,
                         private val onWhirlpoolAdditionTimeChanged: (whirlpoolAdditionTime: Int, index: Int) -> Unit,
                         private val onDryHopStartDayChanged: (day: Int, index: Int) -> Unit,
                         private val onDryHopEndDayChanged: (day: Int, index: Int) -> Unit,
                         private val deleteHopListener: (index: Int) -> Unit) :
    RecyclerView.Adapter<HopEditListAdapter.ViewHolder>() {
    private val hopUses = HopUse.entries.map { it.displayText() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.hop_edit_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun getItemCount() = hopList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hopIngredient = hopList[holder.adapterPosition]
        holder.txtHop.text = hopIngredient.name
        holder.btnDeleteHop.setOnClickListener { deleteHopListener(holder.adapterPosition) }

        initializeHopAmountEditText(holder, hopIngredient)

        initializeHopUseSpinner(holder, hopIngredient)

        initializeBoilAdditionEditText(holder, hopIngredient)
        initializeWhirlpoolEditText(holder, hopIngredient)
        initializeDryHopStartAndEndEditText(holder, hopIngredient)

        setElementVisibilityForHopUse(hopIngredient, holder)
    }

    private fun initializeHopAmountEditText(
        holder: ViewHolder,
        hopIngredient: HopIngredient
    ) {
        with(holder) {
            txtHopAmount.text.insert(0, hopIngredient.amount.toString())
            txtHopAmount.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(hopAmountEditText: Editable?) {
                    val amount = hopAmountEditText.toString().toDoubleOrNull()
                    if (amount != null) {
                        onAmountChanged(amount, adapterPosition)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun initializeHopUseSpinner(
        holder: ViewHolder,
        hopIngredient: HopIngredient
    ) {
        with(holder) {
            hopUseSpinner.adapter = ArrayAdapter(
                itemView.context,
                android.R.layout.simple_spinner_dropdown_item, hopUses
            )
            hopUseSpinner.setSelection(HopUse.entries.indexOf(hopIngredient.use))
            hopUseSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        hopIngredient.use = HopUse.entries[position]
                        setElementVisibilityForHopUse(hopIngredient, holder)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
        }
    }

    private fun initializeBoilAdditionEditText(
        holder: ViewHolder,
        hopIngredient: HopIngredient
    ) {
        with(holder) {
            txtHopBoilAdditionTime.text.insert(0, hopIngredient.boilAdditionTime.toString())
            txtHopBoilAdditionTime.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(hopBoilAdditionTimeEditText: Editable?) {
                    val boilAdditionTime = hopBoilAdditionTimeEditText.toString().toIntOrNull()
                    if (boilAdditionTime != null)
                        onBoilAdditionTimeChanged(boilAdditionTime, adapterPosition)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun initializeWhirlpoolEditText(
        holder: ViewHolder,
        hopIngredient: HopIngredient
    ) {
        with(holder) {
            txtWhirlpoolAdditionTime.text.insert(0, hopIngredient.whirlpoolDuration.toString())
            txtWhirlpoolAdditionTime.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(hopWhirlpoolAdditionTimeEditText: Editable?) {
                    val whirlpoolAdditionTime =
                        hopWhirlpoolAdditionTimeEditText.toString().toIntOrNull()
                    if (whirlpoolAdditionTime != null)
                        onWhirlpoolAdditionTimeChanged(
                            whirlpoolAdditionTime,
                            adapterPosition
                        )
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun initializeDryHopStartAndEndEditText(
        holder: ViewHolder,
        hopIngredient: HopIngredient
    ) {
        with(holder) {
            txtDryHopDayStartEdit.text.insert(0, hopIngredient.dryHopDayStart.toString())
            txtDryHopDayStartEdit.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(hopDryHopDayStartEditText: Editable?) {
                    val day = hopDryHopDayStartEditText.toString().toIntOrNull()
                    if (day != null)
                        onDryHopStartDayChanged(day, adapterPosition)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            txtDryHopDayEndEdit.text.insert(0, hopIngredient.dryHopDayEnd.toString())
            txtDryHopDayEndEdit.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(hopDryHopDayEndEditText: Editable?) {
                    val day = hopDryHopDayEndEditText.toString().toIntOrNull()
                    if (day != null)
                        onDryHopEndDayChanged(day, adapterPosition)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun setElementVisibilityForHopUse(hopIngredient: HopIngredient, holder: ViewHolder) {
        with(holder) {
            when (hopIngredient.use) {
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