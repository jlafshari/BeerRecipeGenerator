package com.jlafshari.beerrecipegenerator.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipegenerator.R

class RecipeListAdapter(private val recipeList: List<RecipePreview>,
                        private val clickListener: (RecipePreview) -> Unit) :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipePreview = recipeList[position]
        holder.txtName.text = recipePreview.toString()
        holder.bind(recipePreview, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = recipeList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)

        fun bind(recipePreview: RecipePreview, clickListener: (RecipePreview) -> Unit) =
            itemView.setOnClickListener { clickListener(recipePreview) }
    }
}