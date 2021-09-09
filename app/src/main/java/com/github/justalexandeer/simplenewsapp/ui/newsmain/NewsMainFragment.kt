package com.github.justalexandeer.simplenewsapp.ui.newsmain

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.databinding.FragmentNewsMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NewsMainFragment : Fragment() {

    // Заменить все статичные данные связанны с темами новостей ( чтобы была возможно настройки тем
    // и их динамиченое отображение)

    private lateinit var binding: FragmentNewsMainBinding
    private val viewModel: NewsMainViewModel by viewModels()
    val listViewOfNews: MutableList<ViewGroup> = mutableListOf()

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
        inflateThemeCards()
        viewModel.getNews()

    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect {
                when(it.mainNewsState) {
                    is MainContractNewsMain.MainNewsState.Idle -> {
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                    is MainContractNewsMain.MainNewsState.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                    is MainContractNewsMain.MainNewsState.Loading -> {
                        it.mainNewsState.listNews?.let {
                            setupNewsInLayout(it)
                        }
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MainContractNewsMain.MainNewsState.Success -> {
                        setupNewsInLayout(it.mainNewsState.listNews)
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.effect.collect{
                when(it) {
                    is MainContractNewsMain.Effect.ShowToast -> {
                        Toast.makeText(this@NewsMainFragment.context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    fun inflateThemeCards() {
        val listOfTheme = listOf("Country", "Health", "Policy")

        val linearLayout = binding.linearLayout as ViewGroup


        for (i in 0..2) {
            val card = layoutInflater.inflate(R.layout.new_card, linearLayout, true) as ViewGroup
            val linearLayoutCard = card.getChildAt(i) as ViewGroup
            val textViewTheme = linearLayoutCard.getChildAt(0) as TextView
            textViewTheme.text = listOfTheme[i]

            val cardView = linearLayoutCard.getChildAt(1) as ViewGroup
            val linearLayoutCardView = cardView.getChildAt(0) as ViewGroup

            for (t in 0..2) {
                val new = layoutInflater.inflate(R.layout.new_view_item2,
                    linearLayoutCardView,
                    false)
                linearLayoutCardView.addView(new)

                val newViewGroup = new as LinearLayout

                listViewOfNews.add(newViewGroup)
            }
        }
    }

    fun setupNewsInLayout(data: List<ArticleDb>) {

        when (data[0].query) {
            "Country" -> {
                val listCountryView = listOf(listViewOfNews[0], listViewOfNews[1], listViewOfNews[2])
                superViewOfNews(data, listCountryView)
            }


        }


            /*
            // val cardView = linearLayoutCard.getChildAt(1) as ViewGroup
            // val linearLayoutCardView = cardView.getChildAt(0) as ViewGroup

            val new = layoutInflater.inflate(R.layout.new_view_item2, linearLayoutCardView, true) as ViewGroup
            val constraintLayoutNew = new.getChildAt(0) as ViewGroup
            val textViewAuthor = constraintLayoutNew.getChildAt(0) as TextView
            textViewAuthor.text = "АВТОР"
            val textViewTitle = constraintLayoutNew.getChildAt(1) as TextView
            textViewTitle.text = "Тайтал"
            */

    }


    fun superViewOfNews(data: List<ArticleDb>, listCountyView: List<ViewGroup>) {
        listCountyView.forEachIndexed { index, viewGroup ->
            val textViewAuthor = listCountyView[index].getChildAt(0) as TextView
            textViewAuthor.text = data[index].author
            val textViewTitle = listCountyView[index].getChildAt(1) as TextView
            textViewTitle.text = data[index].title
        }
    }

    /*fun setupNewsInLayout(data: List<ArticleDb>) {
        when (data[0].query) {
            "Country" -> {
                binding.firstCard.nameTheme.text = "Country"

                data.forEachIndexed { index, article ->
                    when(index) {
                        0 -> {
                            binding.firstCard.firstNew.newsViewItemTitle.text = article.title
                            binding.firstCard.firstNew.newsViewItemDescription.text = article.description
                            binding.firstCard.firstNew.newsViewItemAuthor.text = article.author
                            binding.firstCard.firstNew.newsViewItemDate.text = article.publishedAt
                        }
                        1 -> {
                            binding.firstCard.secondNew.newsViewItemTitle.text = article.title
                            binding.firstCard.secondNew.newsViewItemDescription.text = article.description
                            binding.firstCard.secondNew.newsViewItemAuthor.text = article.author
                            binding.firstCard.secondNew.newsViewItemDate.text = article.publishedAt
                        }
                        2 -> {
                            binding.firstCard.thirdNew.newsViewItemTitle.text = article.title
                            binding.firstCard.thirdNew.newsViewItemDescription.text = article.description
                            binding.firstCard.thirdNew.newsViewItemAuthor.text = article.author
                            binding.firstCard.thirdNew.newsViewItemDate.text = article.publishedAt
                        }
                    }
                }
            }
            "Health" -> {
                binding.secondCard.nameTheme.text = "Health"

                data.forEachIndexed { index, article ->
                    when(index) {
                        0 -> {
                            binding.secondCard.firstNew.newsViewItemTitle.text = article.title
                            binding.secondCard.firstNew.newsViewItemDescription.text = article.description
                            binding.secondCard.firstNew.newsViewItemAuthor.text = article.author
                            binding.secondCard.firstNew.newsViewItemDate.text = article.publishedAt
                        }
                        1 -> {
                            binding.secondCard.secondNew.newsViewItemTitle.text = article.title
                            binding.secondCard.secondNew.newsViewItemDescription.text = article.description
                            binding.secondCard.secondNew.newsViewItemAuthor.text = article.author
                            binding.secondCard.secondNew.newsViewItemDate.text = article.publishedAt
                        }
                        2 -> {
                            binding.secondCard.thirdNew.newsViewItemTitle.text = article.title
                            binding.secondCard.thirdNew.newsViewItemDescription.text = article.description
                            binding.secondCard.thirdNew.newsViewItemAuthor.text = article.author
                            binding.secondCard.thirdNew.newsViewItemDate.text = article.publishedAt
                        }
                    }
                }
            }
            "Policy" -> {
                binding.thirdCard.nameTheme.text = "Policy"

                data.forEachIndexed { index, article ->
                    when(index) {
                        0 -> {
                            binding.thirdCard.firstNew.newsViewItemTitle.text = article.title
                            binding.thirdCard.firstNew.newsViewItemDescription.text = article.description
                            binding.thirdCard.firstNew.newsViewItemAuthor.text = article.author
                            binding.thirdCard.firstNew.newsViewItemDate.text = article.publishedAt
                        }
                        1 -> {
                            binding.thirdCard.secondNew.newsViewItemTitle.text = article.title
                            binding.thirdCard.secondNew.newsViewItemDescription.text = article.description
                            binding.thirdCard.secondNew.newsViewItemAuthor.text = article.author
                            binding.thirdCard.secondNew.newsViewItemDate.text = article.publishedAt
                        }
                        2 -> {
                            binding.thirdCard.thirdNew.newsViewItemTitle.text = article.title
                            binding.thirdCard.thirdNew.newsViewItemDescription.text = article.description
                            binding.thirdCard.thirdNew.newsViewItemAuthor.text = article.author
                            binding.thirdCard.thirdNew.newsViewItemDate.text = article.publishedAt
                        }
                    }
                }
            }
        }
    }*/

    companion object {
        private const val TAG = "NewsMainFragment"
    }

}