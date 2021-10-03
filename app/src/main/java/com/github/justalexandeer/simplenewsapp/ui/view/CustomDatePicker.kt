package com.github.justalexandeer.simplenewsapp.ui.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.justalexandeer.simplenewsapp.ui.newsline.filersettings.FilterSettingFragment
import java.util.*

class CustomDatePicker : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val args: CustomDatePickerArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val date = "$year-${month+1}-$day"

        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            args.dateKey, date)
    }
}