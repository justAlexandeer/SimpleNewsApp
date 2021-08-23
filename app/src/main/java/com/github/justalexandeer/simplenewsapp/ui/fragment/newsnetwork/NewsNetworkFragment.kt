package com.github.justalexandeer.simplenewsapp.ui.fragment.newsnetwork

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.databinding.FragmentNewsNetworkBinding
import com.github.justalexandeer.simplenewsapp.ui.viewmodel.NewsNetworkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsNetworkFragment : Fragment(R.layout.fragment_news_network) {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentNewsNetworkBinding
    private val viewModel: NewsNetworkViewModel by viewModels()
    private val pagingAdapter = NewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsNetworkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        setupRecyclerView()
        setupObservers()
        startReceivingNews()
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = pagingAdapter.withLoadStateFooter(
            footer = NewsLoadStateAdapter{ pagingAdapter.retry() }
        )
    }

    private fun setupObservers() {
        viewModel.listArticles.observe(viewLifecycleOwner, {
            GlobalScope.launch {
                pagingAdapter.submitData(it)
            }
        })
    }

    private fun startReceivingNews() {
        viewModel.getAllNews()
    }

    companion object {
        private const val TAG = "NewsNetworkFragment"
    }
}