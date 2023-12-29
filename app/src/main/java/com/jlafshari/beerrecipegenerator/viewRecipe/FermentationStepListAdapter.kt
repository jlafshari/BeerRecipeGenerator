package com.jlafshari.beerrecipegenerator.viewRecipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.FermentationStep
import com.jlafshari.beerrecipegenerator.R

class FermentationStepListAdapter(private val fermentationStepList: List<FermentationStep>,
    private val context: Context) : RecyclerView.Adapter<FermentationStepListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fermentation_step_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = fermentationStepList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fermentationStep = fermentationStepList[position]
        holder.txtFermentationStep.text = context.getString(R.string.recipe_view_fermentation_step,
            fermentationStep.durationDays.toString(), fermentationStep.temperature.toString())
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtFermentationStep: TextView = itemView.findViewById(R.id.txtFermentationStep)
    }
}