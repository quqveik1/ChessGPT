package com.kurlic.chessgpt.game

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.kurlic.chessgpt.R
import com.kurlic.chessgpt.localgames.LocalGame
import kotlinx.coroutines.launch

open class NewGameCreateDialog {
    lateinit var toogleBar: ToggleButton

    fun show(context: Context, layoutInflater: LayoutInflater) {
        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.localgamecreate_view, null)

        toogleBar = view.findViewById(R.id.sideToogleButton)

        val alertDialog = builder.setView(view)
        alertDialog.setPositiveButton(context.getString(R.string.ok))
        { dialog, id ->
            val name = view.findViewById<EditText>(R.id.editGameName).text.toString();
            val bundle = Bundle()
            bundle.putBoolean(GameFragment.BOTTOMSIDE_KEY, toogleBar.isChecked)

            onOk(bundle, name)

        }

        alertDialog.setNegativeButton(context.getString(R.string.cancel))
        { dialog, id ->
        }

        val alert = alertDialog.create()
            .apply {
                show()
            }

        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.textColor))
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.textColor))
    }

    open fun onOk(bundle: Bundle, name: String) {
    }
}