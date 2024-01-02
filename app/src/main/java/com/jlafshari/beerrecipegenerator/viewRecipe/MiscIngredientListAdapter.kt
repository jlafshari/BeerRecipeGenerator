package com.jlafshari.beerrecipegenerator.viewRecipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.MiscellaneousIngredient
import com.jlafshari.beerrecipegenerator.R

class MiscIngredientListAdapter(private val miscIngredientList: List<MiscellaneousIngredient>,
                                private val context: Context) :
    RecyclerView.Adapter<MiscIngredientListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.misc_ingredient_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = miscIngredientList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mi = miscIngredientList[position]
        holder.txtMiscIngredient.text = context.getString(R.string.recipe_view_misc_ingredient,
            mi.amount.toString(), mi.unit, mi.name, mi.type)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtMiscIngredient: TextView = itemView.findViewById(R.id.txtMiscIngredient)
    }
}