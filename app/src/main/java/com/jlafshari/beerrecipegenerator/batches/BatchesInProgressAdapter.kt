package com.jlafshari.beerrecipegenerator.batches

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.batches.BatchPreview
import com.jlafshari.beerrecipecore.batches.BatchStatus
import com.jlafshari.beerrecipecore.utility.DateUtility
import com.jlafshari.beerrecipegenerator.R

class BatchesInProgressAdapter(
    private val onBatchClick: (String) -> Unit
) : RecyclerView.Adapter<BatchesInProgressAdapter.BatchViewHolder>() {

    private val batches = mutableListOf<BatchPreview>()

    fun updateBatches(newBatches: List<BatchPreview>) {
        batches.clear()
        batches.addAll(newBatches)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_batch_preview, parent, false)
        return BatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        holder.bind(batches[position])
    }

    override fun getItemCount() = batches.size

    inner class BatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtRecipeName: TextView = itemView.findViewById(R.id.txtBatchRecipeName)
        private val txtStatus: TextView = itemView.findViewById(R.id.txtBatchStatus)
        private val txtDate: TextView = itemView.findViewById(R.id.txtBatchDate)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onBatchClick(batches[adapterPosition].id)
                }
            }
        }

        fun bind(batch: BatchPreview) {
            txtRecipeName.text = batch.recipeName
            txtStatus.text = batch.status.name
            txtDate.text = itemView.context.getString(R.string.brewed_on,
                DateUtility.getFormattedDateShortMonthNoYear(batch.brewingDate))
            
            // Set status background color based on status
            val context = itemView.context
            val statusColor = when (batch.status) {
                BatchStatus.Brewing -> R.color.brewing_status
                BatchStatus.Fermenting -> R.color.fermenting_status
                BatchStatus.Carbonating -> R.color.carbonating_status
                else -> R.color.colorPrimary
            }
            txtStatus.setBackgroundColor(context.getColor(statusColor))
        }
    }
}
