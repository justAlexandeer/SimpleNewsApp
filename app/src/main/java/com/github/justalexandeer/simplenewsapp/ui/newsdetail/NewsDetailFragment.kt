package com.github.justalexandeer.simplenewsapp.ui.newsdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.databinding.FragmentNewsDetailBinding
import com.github.justalexandeer.simplenewsapp.util.dateConverter

class NewsDetailFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailBinding
    private val args: NewsDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newsTitle.text = args.news.title
        binding.newsContent.text = args.news.content
        binding.newsAuthor.text = args.news.author
        binding.newsDate.text = dateConverter(args.news.publishedAt)

        Glide.with(view)
            .load(args.news.urlToImage)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ic_error)
            .into(binding.expandedImage)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}