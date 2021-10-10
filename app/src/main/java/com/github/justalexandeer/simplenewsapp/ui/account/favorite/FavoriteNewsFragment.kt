package com.github.justalexandeer.simplenewsapp.ui.account.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.justalexandeer.simplenewsapp.NavGraphDirections
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.databinding.FragmentFavoriteNewsBinding
import com.github.justalexandeer.simplenewsapp.ui.base.OnNewsClickedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FavoriteNewsFragment: Fragment(), OnNewsClickedListener {

    private lateinit var binding: FragmentFavoriteNewsBinding
    private val viewModel: FavoriteNewsViewModel by viewModels()
    private val adapterRecyclerView =  FavoriteNewsRecyclerViewAdapter(this@FavoriteNewsFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun onNewsClick(news: ArticleDb) {
        val action = NavGraphDirections.actionGlobalNewsDetailFragment(news)
        findNavController().navigate(action)
    }

    private fun setupUI() {
        setupRecyclerView()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.listFavoriteNews.value.collect {
                Log.i(TAG, "setupObservers: ${it.size}")
                adapterRecyclerView.submitList(it)
            }
        }
    }

    private fun setupRecyclerView() {
        with(binding.listFavoriteNews) {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterRecyclerView
        }
    }

    companion object {
        private const val TAG = "FavoriteNewsFragment"
    }
}