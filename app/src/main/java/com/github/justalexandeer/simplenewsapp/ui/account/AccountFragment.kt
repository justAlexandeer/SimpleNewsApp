package com.github.justalexandeer.simplenewsapp.ui.account

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.data.sharedpreferences.SharedPreferencesManager
import com.github.justalexandeer.simplenewsapp.databinding.FragmentAccountBinding
import com.github.justalexandeer.simplenewsapp.util.MainNewsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val viewModel: AccountViewModel by viewModels()

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonChangeThemes.setOnClickListener {
            showDialogFragmentWithThemes()
        }
    }

    private fun showDialogFragmentWithThemes() {
        val arrayAllTheme: Array<CharSequence> = MainNewsTheme.values().map {
            it.toString()
        }.toTypedArray()


        val selectedItems = mutableListOf<Int>()
        val selectedTheme = mutableSetOf<String>()
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.alert_dialog_theme_title)
            .setMultiChoiceItems(arrayAllTheme, null,
                DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if (isChecked) {
                        selectedItems.add(which)
                    } else if (selectedItems.contains(which)) {
                        selectedItems.remove(which)
                    }
                })
            .setPositiveButton(R.string.alert_dialog_theme_positive_button,
                DialogInterface.OnClickListener { dialog, id ->
                    selectedItems.forEach {
                        selectedTheme.add(arrayAllTheme[it].toString())
                    }
                    sharedPreferencesManager.setSelectedTheme(
                        SharedPreferencesManager.SELECTED_THEMES,
                        selectedTheme
                    )
                })
            .setNegativeButton(R.string.alert_dialog_theme_negative_button,
                DialogInterface.OnClickListener { dialog, id ->
                })

        builder.create()

        builder.show()
    }

    companion object {
        private const val TAG = "AccountFragment"
    }

}