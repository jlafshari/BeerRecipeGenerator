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
    private var mCallback: OnRecipeSizeSetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_recipe_size, container, false)

        val recipeSizeEditText = view.findViewById<EditText>(R.id.recipeSizeEditText)
        recipeSizeEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val recipeSize = s.toString().toDoubleOrNull()
                mCallback?.onRecipeSizeSet(recipeSize)
            }
        })

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

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    interface OnRecipeSizeSetListener {
        fun onRecipeSizeSet(recipeSize: Double?)
    }
}
