package com.github.justalexandeer.simplenewsapp.ui.newsmain

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.justalexandeer.simplenewsapp.databinding.FragmentNewsMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        lifecycleScope.launch {
            viewModel.uiState.collect {
                when(it.mainNewsState) {
                    is MainContractNewsMain.MainNewsState.Idle -> {
                        Log.i(TAG, "setupObservers: IDLE")
                    }
                    is MainContractNewsMain.MainNewsState.Error -> {
                        Log.i(TAG, "setupObservers: Error")
                    }
                    is MainContractNewsMain.MainNewsState.Loading -> {
                        Log.i(TAG, "setupObservers: Loading")
                    }
                    is MainContractNewsMain.MainNewsState.Success -> {
                        Log.i(TAG, "setupObservers: Success")
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "NewsMainFragment"
    }

}