package com.github.justalexandeer.simplenewsapp.ui.walkthrough.onboardingslide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.justalexandeer.simplenewsapp.databinding.FragmentSearchBinding
import com.github.justalexandeer.simplenewsapp.ui.walkthrough.OnboardingViewModel

class SearchFragment: Fragment() {

    lateinit var binding: FragmentSearchBinding
    private val viewModel: OnboardingViewModel by viewModels({requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                viewModel.onNextButtonClick.send(true);
            }
        }
    }

}