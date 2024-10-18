package com.jlafshari.beerrecipegenerator.viewRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.batches.BatchPreview
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
        holder.txtBatch.text = "${batch.getFormattedBrewingDate()} (${batch.status})"

        holder.btnViewBatch.setOnClickListener { clickListener(batch) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtBatch: TextView = itemView.findViewById(R.id.txtBatch)
        val btnViewBatch: Button = itemView.findViewById(R.id.btnViewBatch)
    }
}