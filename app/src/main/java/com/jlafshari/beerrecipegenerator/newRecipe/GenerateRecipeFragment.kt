package com.jlafshari.beerrecipegenerator.newRecipe

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.RecipeValidationResult


class GenerateRecipeFragment : Fragment() {
    private lateinit var mCallback: OnGenerateRecipeCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_save_recipe, container, false)

        val txtRecipeName = view.findViewById<TextView>(R.id.txt_recipe_name)
        txtRecipeName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(recipeNameEditText: Editable?) {
                mCallback.onRecipeNameSet(recipeNameEditText.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val saveButton = view.findViewById<Button>(R.id.button_generate_recipe)
        saveButton.setOnClickListener {
            saveButton.isEnabled = false
            val recipeValidationResult = mCallback.onGenerateRecipe()
            if (!recipeValidationResult.succeeded) {
                val txtError = view.findViewById<TextView>(R.id.txt_error)
                txtError.text = recipeValidationResult.message
                saveButton.isEnabled = true
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnGenerateRecipeCallback)
            mCallback = context
        else
            throw ClassCastException(context.toString())
    }

    interface OnGenerateRecipeCallback {
        fun onRecipeNameSet(recipeName: String)
        fun onGenerateRecipe() : RecipeValidationResult
    }
}