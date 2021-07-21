package com.jlafshari.beerrecipegenerator.newRecipe

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

class RecipeSizeFragment : Fragment() {
    private lateinit var mCallback: OnRecipeSizeSetListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipe_size, container, false)

        val recipeSizeEditText = view.findViewById<EditText>(R.id.recipeSizeEditText)
        recipeSizeEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val recipeSize = s.toString().toDoubleOrNull()
                mCallback.onRecipeSizeSet(recipeSize)
            }
        })
        mCallback.onRecipeSizeSet(recipeSizeEditText.text.toString().toDoubleOrNull())

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRecipeSizeSetListener) {
            mCallback = context
        } else {
            throw ClassCastException("$context must implement OnRecipeSizeSetListener")
        }
    }

    interface OnRecipeSizeSetListener {
        fun onRecipeSizeSet(recipeSize: Double?)
    }
}
