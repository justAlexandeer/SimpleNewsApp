package com.github.justalexandeer.simplenewsapp.ui.fragment.newsline

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.justalexandeer.simplenewsapp.databinding.FragmentNewsLineBinding
import com.github.justalexandeer.simplenewsapp.ui.viewmodel.NewsLineViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsLineFragment : Fragment() {

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
        viewModel.getArticles()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = pagingAdapter.withLoadStateFooter(
            footer = NewsLoadStateAdapter { pagingAdapter.retry() }
        )
    }

    private fun setupObservers() {
        viewModel.listArticle.observe(viewLifecycleOwner, {
            GlobalScope.launch {
                pagingAdapter.submitData(it)
            }
        })
    }

    companion object {
        private const val TAG = "NewsCacheFragment"
    }

}