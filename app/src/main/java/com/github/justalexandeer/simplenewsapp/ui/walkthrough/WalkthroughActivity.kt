package com.github.justalexandeer.simplenewsapp.ui.walkthrough

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.databinding.ActivityWalkthroughBinding
import com.github.justalexandeer.simplenewsapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

// Исправить Activity и ViewModel под MVVM

@AndroidEntryPoint
class WalkthroughActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalkthroughBinding
    private val viewModel: WalkthroughViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SimpleNewsApp)
        super.onCreate(savedInstanceState)
        binding = ActivityWalkthroughBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.checkIsFirstStart()
        setupObserver()
        binding.button.setOnClickListener {
            viewModel.onButtonClick()
        }
    }

    private fun onMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            viewModel.isNeedStartNewActivity
                .filter {
                    return@filter it
                }
                .collect {
                    onMainActivity()
                }
        }

        lifecycleScope.launch {
            viewModel.isDefaultThemeSet
                .combine(viewModel.isButtonClick) { isThemeSet, isButtonClick ->
                    return@combine isThemeSet && isButtonClick
                }
                .filter {
                    return@filter it
                }
                .collect {
                    onMainActivity()
                }
        }

    }

    companion object {
        private const val TAG = "WalkthroughActivity"
    }

}