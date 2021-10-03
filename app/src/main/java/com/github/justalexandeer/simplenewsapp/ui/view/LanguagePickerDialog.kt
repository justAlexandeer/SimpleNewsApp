package com.github.justalexandeer.simplenewsapp.ui.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.data.model.Language
import com.google.android.material.dialog.InsetDialogOnTouchListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LanguagePickerDialog : DialogFragment() {

    private val args: LanguagePickerDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arrayOfLanguage = Language.values().map {
            it.toString()
        }.toTypedArray()
        var selectedItem = arrayOfLanguage.indexOf(args.selectedItem)
        val builder =
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.filter_setting_language_header)
                .setNeutralButton(R.string.alert_dialog_negative_button) { _, which ->

                }
                .setPositiveButton(R.string.alert_dialog_positive_button) { dialog, which ->
                    val language =
                        Language.getValueByLanguageToString(arrayOfLanguage[selectedItem])
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        args.languageKey, language
                    )
                }
                .setSingleChoiceItems(arrayOfLanguage, selectedItem) { _, which ->
                    selectedItem = which
                }

        return builder.create()
    }
}