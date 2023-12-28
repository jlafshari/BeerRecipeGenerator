package com.jlafshari.beerrecipegenerator.viewRecipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.HopIngredient
import com.jlafshari.beerrecipecore.HopUse
import com.jlafshari.beerrecipegenerator.R

class HopListAdapter(private val hopList: List<HopIngredient>, private val context: Context) :
    RecyclerView.Adapter<HopListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.hop_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hopIngredient = hopList[position]
        holder.txtHop.text = getHopDisplayText(hopIngredient)
    }

    override fun getItemCount() = hopList.size

    private fun getHopDisplayText(hopIngredient: HopIngredient): String {
        return when (hopIngredient.use) {
            HopUse.Boil -> {
                context.getString(
                    R.string.hopListBoilItem, hopIngredient.amount.toString(), hopIngredient.name,
                    hopIngredient.boilAdditionTime.toString())
            }
            HopUse.Whirlpool -> {
                context.getString(
                    R.string.hopListWhirlpoolItem, hopIngredient.amount.toString(), hopIngredient.name,
                    hopIngredient.whirlpoolDuration.toString())
            }
            HopUse.DryHop -> {
                context.getString(R.string.hopListDryHopItem, hopIngredient.amount.toString(),
                    hopIngredient.name, hopIngredient.dryHopDayStart.toString(), hopIngredient.dryHopDayEnd.toString())
            }
            else -> {
                hopIngredient.name
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtHop: TextView = itemView.findViewById(R.id.txtHop)
    }
}