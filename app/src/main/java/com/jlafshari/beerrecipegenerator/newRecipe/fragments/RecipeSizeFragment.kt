package com.jlafshari.beerrecipegenerator.newRecipe.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.jlafshari.beerrecipegenerator.R
import com.jlafshari.beerrecipegenerator.newRecipe.RecipeInfoListener
import com.jlafshari.beerrecipegenerator.settings.AppSettings

class RecipeSizeFragment : Fragment() {
    private lateinit var mCallback: OnRecipeSizeSetListener
    private lateinit var mRecipeSizeEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipe_size, container, false)

        mRecipeSizeEditText = view.findViewById(R.id.recipeSizeEditText)
        mRecipeSizeEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val recipeSize = s.toString().toDoubleOrNull()
                mCallback.onRecipeSizeSet(recipeSize)
            }
        })
        mCallback.onRecipeSizeSet(mRecipeSizeEditText.text.toString().toDoubleOrNull())

        return view
    }

    override fun onResume() {
        val recipeGenerationInfo = mCallback.getCurrentRecipeGenerationInfo()
        val recipeSize = if (recipeGenerationInfo.size != null) {
            recipeGenerationInfo.size.toString()
        }
        else {
            AppSettings.recipeDefaultSettings.size.toString()
        }
        mRecipeSizeEditText.text.clear()
        mRecipeSizeEditText.text.insert(0, recipeSize)
        mCallback.onRecipeSizeSet(mRecipeSizeEditText.text.toString().toDoubleOrNull())
        super.onResume()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRecipeSizeSetListener) {
            mCallback = context
        } else {
            throw ClassCastException("$context must implement OnRecipeSizeSetListener")
        }
    }

    interface OnRecipeSizeSetListener : RecipeInfoListener {
        fun onRecipeSizeSet(recipeSize: Double?)
    }
}
