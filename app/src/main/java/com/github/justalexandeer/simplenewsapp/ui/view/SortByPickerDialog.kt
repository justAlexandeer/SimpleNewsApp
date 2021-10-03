package com.github.justalexandeer.simplenewsapp.ui.view

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.data.model.SortBy
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SortByPickerDialog : DialogFragment() {

    val args: SortByPickerDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arrayOfSortBy = SortBy.values().map {
            it.toString()
        }.toTypedArray()
        var selectedItem = arrayOfSortBy.indexOf(args.selectedItem)
        val builder =
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.filter_setting_sort_by_header)
                .setNeutralButton(R.string.alert_dialog_negative_button) { _, which ->

                }
                .setPositiveButton(R.string.alert_dialog_positive_button) {_, which ->
                    val sortBy = SortBy.getValueBySortToString(arrayOfSortBy[selectedItem])
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        args.sortByKey, sortBy
                    )
                }
                .setSingleChoiceItems(arrayOfSortBy, selectedItem) { _, which ->
                    selectedItem = which
                }

        return builder.create()
    }

}