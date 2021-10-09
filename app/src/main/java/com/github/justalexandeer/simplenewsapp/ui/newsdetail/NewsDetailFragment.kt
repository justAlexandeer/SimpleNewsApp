package com.github.justalexandeer.simplenewsapp.ui.newsdetail

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.databinding.FragmentNewsDetailBinding
import com.github.justalexandeer.simplenewsapp.util.dateConverter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailBinding
    private val args: NewsDetailFragmentArgs by navArgs()
    private val viewModel: NewsDetailViewModel by viewModels()

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

        setupUI(view)
    }

    private fun setupUI(view: View) {
        setupActionBar()
        setupView(view)
    }

    private fun setupActionBar() {
        viewModel.isUserAddNewsToFavorite(args.news.title, args.news.content, args.news.url)
        lifecycleScope.launchWhenCreated {
            viewModel.newsIsFavorite.collect {
                it?.let {
                    binding.toolbar.menu.clear()
                    binding.toolbar.menu.findItem(R.id.news_detail_menu_favorite).isEnabled =
                        true
                    binding.toolbar.inflateMenu(R.menu.news_detail_menu)
                    if (it) {
                        binding.toolbar.menu.findItem(R.id.news_detail_menu_favorite)
                            .setIcon(R.drawable.outline_star_black_36dp)
                        binding.toolbar.setOnMenuItemClickListener { menu ->
                            when (menu.itemId) {
                                R.id.news_detail_menu_favorite -> {
                                    viewModel.removeNewFromFavorite(args.news)
                                    binding.toolbar.menu.findItem(R.id.news_detail_menu_favorite).isEnabled =
                                        false
                                    true
                                }
                                else -> {
                                    false
                                }
                            }
                        }
                    } else {
                        binding.toolbar.menu.findItem(R.id.news_detail_menu_favorite)
                            .setIcon(R.drawable.outline_star_outline_black_36dp)
                        binding.toolbar.setOnMenuItemClickListener { menu ->
                            when (menu.itemId) {
                                R.id.news_detail_menu_favorite -> {
                                    viewModel.addNewToFavorite(args.news)
                                    binding.toolbar.menu.findItem(R.id.news_detail_menu_favorite).isEnabled =
                                        false
                                    true
                                }
                                else -> {
                                    false
                                }
                            }
                        }
                    }
                }
            }
        }

//        binding.toolbar.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.news_detail_menu_favorite -> {
//                    Toast.makeText(context, "asdf", Toast.LENGTH_SHORT).show()
//                    true
//                }
//                else -> {
//                    false
//                }
//            }
//        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupView(view: View) {
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
    }

    companion object {
        private const val TAG = "NewsDetailFragment"
    }
}