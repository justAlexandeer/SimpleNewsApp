package com.github.justalexandeer.simplenewsapp.ui.walkthrough.onboardingslide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.databinding.FragmentSaveBinding
import com.github.justalexandeer.simplenewsapp.ui.walkthrough.OnboardingViewModel

class SaveFragment: Fragment() {

    lateinit var binding: FragmentSaveBinding
    private val viewModel: OnboardingViewModel by viewModels({requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener {
            viewModel.isButtonStartClick.value = true
        }
    }
}