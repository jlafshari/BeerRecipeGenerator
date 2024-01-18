package com.jlafshari.beerrecipegenerator.recipes

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jlafshari.beerrecipecore.recipes.RecipePreview
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.srmColors.Colors

class RecipeListAdapter(private val recipeList: List<RecipePreview>,
                        private val clickListener: (RecipePreview) -> Unit) :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipePreview = recipeList[position]
        with (holder) {
            setRecipeNameText(recipePreview)
            txtAbv.text = recipePreview.abv.toString() + "%"

            val color = Colors.getColor(recipePreview.colorSrm)
            if (color != null) {
                txtColorSrm.text = color.srmColor.toString()
                srmColorCardView.setCardBackgroundColor(color.rbgColor)
            }

            bind(recipePreview, clickListener)
        }
    }

    private fun ViewHolder.setRecipeNameText(recipePreview: RecipePreview) {
        val nameBuilder = SpannableStringBuilder(recipePreview.toString())
        val nameMaxLength = 28
        if (nameBuilder.length > nameMaxLength) {
            nameBuilder.replace(nameMaxLength - 1, nameBuilder.length, "...")
        }
        txtName.text = nameBuilder
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = recipeList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtAbv: TextView = itemView.findViewById(R.id.txtAbv)
        val txtColorSrm: TextView = itemView.findViewById(R.id.txtColorSrm)
        val srmColorCardView: CardView = itemView.findViewById(R.id.srmColorCardView)

        fun bind(recipePreview: RecipePreview, clickListener: (RecipePreview) -> Unit) =
            itemView.setOnClickListener { clickListener(recipePreview) }
    }
}