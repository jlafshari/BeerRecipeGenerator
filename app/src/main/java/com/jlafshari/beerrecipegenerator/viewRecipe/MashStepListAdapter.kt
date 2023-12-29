package com.jlafshari.beerrecipegenerator.viewRecipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.MashStep
import com.jlafshari.beerrecipegenerator.R

class MashStepListAdapter(private val mashStepList: List<MashStep>, private val context: Context) :
    RecyclerView.Adapter<MashStepListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.mash_step_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = mashStepList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mashStep = mashStepList[position]
        holder.txtMashStep.text = context.getString(R.string.recipe_view_mash_step,
            mashStep.duration.toString(), mashStep.targetTemperature.toString())
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtMashStep: TextView = itemView.findViewById(R.id.txtMashStep)
    }
}