package com.jlafshari.beerrecipegenerator.viewBatch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.batches.FermentationScheduleStep
import com.jlafshari.beerrecipecore.utility.DateUtility
import com.jlafshari.beerrecipegenerator.R

class FermentationScheduleListAdapter(private val fermentationScheduleList: List<FermentationScheduleStep>,
    private val context: Context) :
    RecyclerView.Adapter<FermentationScheduleListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.fermentation_schedule_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun getItemCount() = fermentationScheduleList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fermentationScheduleStep = fermentationScheduleList[position]
        val date = DateUtility.getFormattedDate(fermentationScheduleStep.startDate)
        holder.txtFermentationStep.text = context.getString(
            R.string.fermentation_step_template,
            date,
            fermentationScheduleStep.temperature.toString()
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtFermentationStep: TextView = itemView.findViewById(R.id.txtFermentationStep)
    }
}