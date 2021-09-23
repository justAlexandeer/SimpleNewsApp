package com.github.justalexandeer.simplenewsapp.ui.newsline

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.justalexandeer.simplenewsapp.databinding.FragmentNewsLineBinding
import com.github.justalexandeer.simplenewsapp.ui.newsline.recyclerview.ArticleAdapter
import com.github.justalexandeer.simplenewsapp.ui.newsline.recyclerview.NewsLoadStateAdapter
import com.github.justalexandeer.simplenewsapp.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.security.auth.login.LoginException

@AndroidEntryPoint
class NewsLineFragment : Fragment() {

    // Фокус разобраться

    private lateinit var binding: FragmentNewsLineBinding
    private val viewModel: NewsLineViewModel by viewModels()
    private val pagingAdapter = ArticleAdapter()

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


        setupRecyclerView()
        setupObservers()
        setupTextInputEditText()

        //viewModel.getArticles()
    }

    private fun setupTextInputEditText() {
        binding.textInputEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateNewsList()
                hideKeyboard()
                true
            } else {
                false
            }

        }
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = pagingAdapter.withLoadStateFooter(
            footer = NewsLoadStateAdapter { pagingAdapter.retry() }
        )
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state ->
                when(state) {
                    is ContractNewsLine.State.Idle -> {

                    }
                    is ContractNewsLine.State.Loading -> {
//                        state.listNews?.let {
//                            pagingAdapter.submitData(state.listNews)
//                        }
                    }
                    is ContractNewsLine.State.Success -> {
                        lifecycleScope.launchWhenCreated {
                            pagingAdapter.submitData(state.listNews)
                        }
                    }
                    is ContractNewsLine.State.Error -> {

                    }
                }

            }
        }
    }

    private fun updateNewsList() {
        binding.textInputEditText.text?.trim().toString().let { query ->
            if (query.isNotEmpty()) {
                // add scroll
                viewModel.setEvent(ContractNewsLine.Event.GetNews(query))
            }
        }

    }

    companion object {
        private const val TAG = "NewsCacheFragment"
    }


}