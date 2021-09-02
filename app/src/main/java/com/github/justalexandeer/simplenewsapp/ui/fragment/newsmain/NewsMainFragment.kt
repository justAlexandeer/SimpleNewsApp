package com.github.justalexandeer.simplenewsapp.ui.fragment.newsmain

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.justalexandeer.simplenewsapp.databinding.FragmentNewsMainBinding
import com.github.justalexandeer.simplenewsapp.ui.viewmodel.NewsMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsMainFragment : Fragment() {

    private lateinit var binding: FragmentNewsMainBinding
    private val viewModel: NewsMainViewModel by viewModels()
    private lateinit var group: ViewGroup

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

        setupObservers()
        viewModel.getNews()
    }

    private fun setupObservers() {
        viewModel.mutableListNewsBitcoin.observe(viewLifecycleOwner, {
            val article = it[0]


            binding.firstCard.firstNew.newsViewItemAuthor.text = article.author
            binding.firstCard.firstNew.newsViewItemTitle.text = article.title
            binding.firstCard.firstNew.newsViewItemDescription.text = article.description
            binding.firstCard.firstNew.newsViewItemDate.text = article.publishedAt
        })
    }

    companion object {
        private const val TAG = "NewsMainFragment"
    }

}