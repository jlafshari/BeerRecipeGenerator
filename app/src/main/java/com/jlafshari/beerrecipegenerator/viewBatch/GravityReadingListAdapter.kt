package com.jlafshari.beerrecipegenerator.viewBatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.batches.GravityReading
import com.jlafshari.beerrecipecore.utility.DateUtility
import com.jlafshari.beerrecipegenerator.R

class GravityReadingListAdapter(private val gravityReadingList: List<GravityReading>) :
    RecyclerView.Adapter<GravityReadingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.gravity_reading_item_layout, parent,
            false)
        return ViewHolder(v)
    }

    override fun getItemCount() = gravityReadingList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gravityReading = gravityReadingList[position]
        holder.txtReadingDate.text = DateUtility.getFormattedDate(gravityReading.date)
        holder.txtReadingValue.text = gravityReading.value.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtReadingDate: TextView = itemView.findViewById(R.id.txtReadingDate)
        val txtReadingValue: TextView = itemView.findViewById(R.id.txtReadingValue)
    }
}