package com.github.justalexandeer.simplenewsapp.ui.newsmain

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.data.sharedpreferences.SharedPreferencesManager
import com.github.justalexandeer.simplenewsapp.databinding.FragmentNewsMainBinding
import com.github.justalexandeer.simplenewsapp.ui.newsmain.recyclerview.DataItem
import com.github.justalexandeer.simplenewsapp.ui.newsmain.recyclerview.NewsRecyclerViewAdapter
import com.github.justalexandeer.simplenewsapp.util.MainNewsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class NewsMainFragment : Fragment() {

    private lateinit var binding: FragmentNewsMainBinding
    private val viewModel: NewsMainViewModel by viewModels()
    private val newsRecyclerViewAdapter = NewsRecyclerViewAdapter()

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state ->
                when (state) {
                    is ContractNewsMain.State.Idle -> {
                        viewModel.setEvent(ContractNewsMain.Event.GetMainNews)
                    }
                    is ContractNewsMain.State.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                    is ContractNewsMain.State.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        state.listNews?.let {
                            newsRecyclerViewAdapter.submitList(mapper(it))
                        }
                    }
                    is ContractNewsMain.State.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE

                        state.listNews.let {
                            newsRecyclerViewAdapter.submitList(mapper(it))
                        }
                    }
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.effect.collect {
                when (it) {
                    is ContractNewsMain.Effect.ShowToast -> {
                        Toast.makeText(
                            this@NewsMainFragment.context,
                            it.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = newsRecyclerViewAdapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context)
    }

    private fun mapper(listOfNews: List<ArticleDb>): List<DataItem> {
        val setOfTheme =
            sharedPreferencesManager.getSelectedTheme(SharedPreferencesManager.SELECTED_THEMES)
        val groupedListOfNews = listOfNews.groupBy { it.query }
        val resultList = mutableListOf<DataItem>()

        setOfTheme.forEach { theme ->
            resultList.add(DataItem.NewTheme(theme.toString()))
            groupedListOfNews.forEach { entry ->
                if (theme.toString() == entry.key) {
                    resultList.addAll(
                        entry.value.map {
                            DataItem.NewItem(it)
                        }
                    )
                }
            }
        }

        return resultList
    }


    companion object {
        private const val TAG = "NewsMainFragment"
    }

}