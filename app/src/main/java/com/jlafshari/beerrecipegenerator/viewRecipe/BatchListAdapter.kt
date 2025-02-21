package com.jlafshari.beerrecipegenerator.viewRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.batches.BatchPreview
import com.jlafshari.beerrecipecore.batches.displayText
import com.jlafshari.beerrecipecore.utility.DateUtility
import com.jlafshari.beerrecipegenerator.R

class BatchListAdapter(
    private val batchList: List<BatchPreview>,
    private val clickListener: (BatchPreview) -> Unit
)
    : RecyclerView.Adapter<BatchListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.batch_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun getItemCount() = batchList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val batch = batchList[position]
        val formattedBrewingDate = DateUtility.getFormattedDate(batch.brewingDate)
        holder.txtBatch.text = "${formattedBrewingDate} (${batch.status.displayText()})"

        holder.itemView.setOnClickListener { clickListener(batch) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtBatch: TextView = itemView.findViewById(R.id.txtBatch)
    }
}