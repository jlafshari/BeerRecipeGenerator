package com.jlafshari.beerrecipegenerator

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.ArrayList

class RecipeListAdapter(private val recipeList: ArrayList<RecipePreview>) : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: RecipeListAdapter.ViewHolder, position: Int) {
        holder.txtName.text = recipeList[position].name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = recipeList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
    }
}