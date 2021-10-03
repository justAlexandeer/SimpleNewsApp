package com.github.justalexandeer.simplenewsapp.ui.newsline.filersettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.data.model.Language
import com.github.justalexandeer.simplenewsapp.data.model.SortBy
import com.github.justalexandeer.simplenewsapp.databinding.FragmentFilterSettingBinding

class FilterSettingFragment : Fragment() {

    private val args: FilterSettingFragmentArgs by navArgs()
    private val viewModel: FilterSettingViewModel by viewModels()

    private lateinit var binding: FragmentFilterSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupUI()
        setupObservers()
    }

    private fun setupViewModel() {
        viewModel.setupFilterSetting(args.filterSetting)
    }

    private fun setupUI() {
        setupDateFromView()
        setupDateToView()
        setupLanguageView()
        setupSortByView()
        setupButtonFind()
    }

    private fun setupObservers() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.filterSettingFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                navBackStackEntry.savedStateHandle.getLiveData<String>(KEY_DATE_FROM)
                    .observe(viewLifecycleOwner) {
                        val dateFrom =
                            context?.getString(R.string.filter_setting_date_value_start) + " " + it
                        binding.dateValueStart.text = dateFrom
                        viewModel.currentFilterSetting =
                            viewModel.currentFilterSetting!!.copy(dateFrom = it)
                    }
                navBackStackEntry.savedStateHandle.getLiveData<String>(KEY_DATE_TO)
                    .observe(viewLifecycleOwner) {
                        val dateTo =
                            context?.getString(R.string.filter_setting_date_value_end) + " " + it
                        binding.dateValueEnd.text = dateTo
                        viewModel.currentFilterSetting =
                            viewModel.currentFilterSetting!!.copy(dateTo = it)
                    }
                navBackStackEntry.savedStateHandle.getLiveData<Language>(KEY_LANGUAGE)
                    .observe(viewLifecycleOwner) {
                        val language =
                            context?.getString(R.string.filter_setting_language_value) + " " + it.toString()
                        binding.languageValue.text = language
                        viewModel.currentFilterSetting =
                            viewModel.currentFilterSetting!!.copy(language = it)
                    }
                navBackStackEntry.savedStateHandle.getLiveData<SortBy>(KEY_SORT_BY)
                    .observe(viewLifecycleOwner) {
                        val sortBy = it.toString()
                        binding.sortByValue.text = sortBy
                        viewModel.currentFilterSetting =
                            viewModel.currentFilterSetting!!.copy(sortBy = it)
                    }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            KEY_DATE_FROM
        )?.observe(viewLifecycleOwner) {
            binding.dateValueStart.text = it
        }

    }

    private fun setupDateFromView() {
        with(binding.dateValueStart) {
            text = if (viewModel.currentFilterSetting!!.dateFrom.isEmpty())
                context.getString(R.string.filter_setting_date_value_start) + " " +
                        context.getString(R.string.filter_setting_date_default)
            else
                context.getString(R.string.filter_setting_date_value_start) + " " +
                        viewModel.currentFilterSetting!!.dateFrom
            setOnClickListener {
                val action =
                    FilterSettingFragmentDirections.actionFilterSettingFragmentToCutsomDatePicker(
                        KEY_DATE_FROM
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun setupDateToView() {
        with(binding.dateValueEnd) {
            text = if (viewModel.currentFilterSetting!!.dateTo.isEmpty())
                context.getString(R.string.filter_setting_date_value_end) + " " +
                        context.getString(R.string.filter_setting_date_default)
            else
                context.getString(R.string.filter_setting_date_value_end) + " " +
                        viewModel.currentFilterSetting!!.dateTo
            setOnClickListener {
                val action =
                    FilterSettingFragmentDirections.actionFilterSettingFragmentToCutsomDatePicker(
                        KEY_DATE_TO
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun setupLanguageView() {
        with(binding.languageValue) {
            text = if (viewModel.currentFilterSetting!!.language == Language.DEFAULT)
                context.getString(R.string.filter_setting_language_value) + " " +
                        Language.DEFAULT.toString()
            else
                context.getString(R.string.filter_setting_language_value) + " " +
                        viewModel.currentFilterSetting!!.language.toString()
            setOnClickListener {
                val action =
                    FilterSettingFragmentDirections.actionFilterSettingFragmentToLanguagePickerDialog(
                        KEY_LANGUAGE,
                        viewModel.currentFilterSetting!!.language.toString()
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun setupSortByView() {
        with(binding.sortByValue) {
            text = viewModel.currentFilterSetting!!.sortBy.toString()
            setOnClickListener {
                val action =
                    FilterSettingFragmentDirections.actionFilterSettingFragmentToSortByPickerDialog(
                        KEY_SORT_BY,
                        viewModel.currentFilterSetting!!.sortBy.toString()
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun setupButtonFind() {
        binding.buttonFind.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                KEY_FILTER_SETTING,
                viewModel.currentFilterSetting!!
            )
            findNavController().navigateUp()
        }
    }

    companion object {
        const val KEY_FILTER_SETTING = "filter_setting"
        const val KEY_DATE_FROM = "date_from"
        const val KEY_DATE_TO = "date_to"
        const val KEY_LANGUAGE = "language"
        const val KEY_SORT_BY = "sort_by"
        private const val TAG = "FilterSettingFragment"
    }
}
