package com.github.justalexandeer.simplenewsapp.ui.walkthrough

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.databinding.FragmentOnboardingBinding
import com.github.justalexandeer.simplenewsapp.ui.walkthrough.onboardingslide.ReadFragment
import com.github.justalexandeer.simplenewsapp.ui.walkthrough.onboardingslide.SaveFragment
import com.github.justalexandeer.simplenewsapp.ui.walkthrough.onboardingslide.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    lateinit var binding: FragmentOnboardingBinding
    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.checkIsFirstStart()
        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.isNeedStartOnboarding
                .filter {
                    return@filter !it
                }
                .collect {
                    startNextDestination()
                }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.isDefaultThemeSet
                .combine(viewModel.isButtonStartClick) { isThemeSet, isButtonClick ->
                    return@combine isThemeSet && isButtonClick
                }
                .filter {
                    return@filter it
                }
                .collect {
                    viewModel.changeValueOfFirstStart()
                    startNextDestination()
                }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.onNextButtonClick
                .receiveAsFlow()
                .collect {
                    binding.viewPager.currentItem = binding.viewPager.currentItem + 1
                }
        }
    }

    private fun startNextDestination() {
        findNavController().navigate(R.id.action_onboardingFragment_to_newsLineFragment)
    }

    private inner class ScreenSlidePagerAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ReadFragment()
                1 -> SearchFragment()
                2 -> SaveFragment()
                else -> ReadFragment()
            }
        }
    }

    companion object {
        private const val TAG = "OnboardingFragment"
        private const val NUM_PAGES = 3
    }

}