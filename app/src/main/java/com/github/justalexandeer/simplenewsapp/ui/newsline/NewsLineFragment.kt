package com.github.justalexandeer.simplenewsapp.ui.newsline

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.justalexandeer.simplenewsapp.NavGraphDirections
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.data.sharedpreferences.SharedPreferencesManager
import com.github.justalexandeer.simplenewsapp.databinding.FragmentNewsLineBinding
import com.github.justalexandeer.simplenewsapp.ui.newsline.recyclerview.ArticleAdapter
import com.github.justalexandeer.simplenewsapp.ui.newsline.recyclerview.NewsLoadStateAdapter
import com.github.justalexandeer.simplenewsapp.util.MainNewsTheme
import com.github.justalexandeer.simplenewsapp.util.hideKeyboard
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.github.justalexandeer.simplenewsapp.data.model.FilterSettings
import com.github.justalexandeer.simplenewsapp.ui.base.OnNewsClickedListener
import com.github.justalexandeer.simplenewsapp.ui.newsline.filersettings.FilterSettingFragment.Companion.KEY_FILTER_SETTING

@AndroidEntryPoint
class NewsLineFragment : Fragment(), OnNewsClickedListener {

    private lateinit var binding: FragmentNewsLineBinding
    private val viewModel: NewsLineViewModel by viewModels()
    private val pagingAdapter = ArticleAdapter(this)

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsLineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
    }

    override fun onNewsClick(news: ArticleDb) {
        val action = NavGraphDirections.actionGlobalNewsDetailFragment(news)
        findNavController().navigate(action)
    }

    private fun setupUI() {
        setupRecyclerView()
        setupTextInputEditText()
        setupButton()
        setupChips()
    }

    private fun setupTextInputEditText() {
        binding.textInputEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateNewsList()
                hideKeyboard()
                binding.chipGroup.clearCheck()
                true
            } else {
                false
            }
        }
    }

    private fun setupRecyclerView() {
        binding.retryButton.setOnClickListener {
            pagingAdapter.retry()
        }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = pagingAdapter.withLoadStateFooter(
            footer = NewsLoadStateAdapter { pagingAdapter.retry() }
        )
        lifecycleScope.launchWhenCreated {
            pagingAdapter.loadStateFlow
                .collect { loadState ->
                    if (loadState.refresh is LoadState.Loading) {
                        viewModel.isFirstLoading.value = false
                    }
                    binding.textViewNoResult.isVisible =
                        !viewModel.isFirstLoading.value && loadState.refresh is LoadState.NotLoading
                                && pagingAdapter.itemCount < 1 && loadState.append.endOfPaginationReached
                    binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
                    binding.retryButton.isVisible =
                        loadState.mediator?.refresh is LoadState.Error && pagingAdapter.itemCount == 0
                }
        }
    }

    private fun setupObservers() {
        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<FilterSettings>(KEY_FILTER_SETTING)
            ?.observe(viewLifecycleOwner) { result ->
                viewModel.setEvent(ContractNewsLine.Event.SetFilterSettings(result))
                savedStateHandle.remove<FilterSettings>(KEY_FILTER_SETTING)
                updateNewsList()
            }

        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state ->
                when (state) {
                    is ContractNewsLine.State.Idle -> {
                    }
                    is ContractNewsLine.State.PagingDataState -> {
                        lifecycleScope.launchWhenCreated {
                            pagingAdapter.submitData(state.listNews)
                        }
                    }
                }
            }
        }
    }

    private fun setupChips() {
        MainNewsTheme.values()
            .forEach { theme ->
                val chip = layoutInflater.inflate(
                    R.layout.view_chip_choice,
                    binding.chipGroup,
                    false
                )
                (chip as Chip).apply {
                    text = theme.toString()
                }
                binding.chipGroup.addView(chip)
            }

        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            val chip = binding.root.findViewById<Chip>(checkedId)
            chip?.let {
                updateEditTextAndMakeQuery(chip.text.toString())
            }
        }

    }

    private fun setupButton() {
        binding.buttonFilterSettings.setOnClickListener {
            val action = NewsLineFragmentDirections.actionNewsLineFragmentToFilterSettingFragment(
                viewModel.currentFilterSettings1.value
            )
            findNavController().navigate(action)
        }
    }

    private fun updateEditTextAndMakeQuery(query: String) {
        binding.textInputEditText.setText(query)
        updateNewsList()
    }

    private fun updateNewsList() {
        binding.textInputEditText.text?.trim().toString().let { query ->
            if (query.isNotEmpty()) {
                viewModel.setEvent(ContractNewsLine.Event.GetNews(query))
            }
        }
    }

    companion object {
        private const val TAG = "NewsCacheFragment"
    }


}