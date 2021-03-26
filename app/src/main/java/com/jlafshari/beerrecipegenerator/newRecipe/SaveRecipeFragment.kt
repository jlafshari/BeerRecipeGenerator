package com.jlafshari.beerrecipegenerator.newRecipe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jlafshari.beerrecipegenerator.R

class SaveRecipeFragment : Fragment() {
    private var mCallback: OnSaveRecipeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_save_recipe, container, false)

        val saveButton = view.findViewById<Button>(R.id.button_save_recipe)
        saveButton.setOnClickListener {
            val txtRecipeName = view.findViewById<TextView>(R.id.txt_recipe_name)
            mCallback?.onSaveRecipe(txtRecipeName.text.toString())
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnSaveRecipeListener)
            mCallback = context
        else
            throw ClassCastException(context.toString())
    }

    interface OnSaveRecipeListener {
        fun onSaveRecipe(recipeName: String)
    }
}