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
import com.github.justalexandeer.simplenewsapp.data.sharedpreferences.SharedPreferencesManager
import com.github.justalexandeer.simplenewsapp.databinding.FragmentNewsMainBinding
import com.github.justalexandeer.simplenewsapp.util.COUNT_NEWS_IN_CARD_THEME
import com.github.justalexandeer.simplenewsapp.util.MainNewsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class NewsMainFragment : Fragment() {

    // Заменить все статичные данные связанны с темами новостей (чтобы была возможно настройки тем
    // и их динамиченое отображение)

    private lateinit var binding: FragmentNewsMainBinding
    private val viewModel: NewsMainViewModel by viewModels()
    private val mapViewOfNews: MutableMap<ViewGroup, String> = mutableMapOf()

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager


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

        inflateThemeCards()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state ->
                when (state) {
                    is ContractNewsMain.State.Idle -> {
                        viewModel.setEvent(ContractNewsMain.Event.GetMainNews)
                    }
                    is ContractNewsMain.State.Error -> {
                        Log.i(TAG, "setupObservers: ${state.errorMessage}")
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                    is ContractNewsMain.State.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        state.listNews?.let {
                            setupNewsInLayout(
                                it,
                                mapViewOfNews,
                                sharedPreferencesManager.getSelectedTheme(SharedPreferencesManager.SELECTED_THEMES)
                            )
                        }
                    }
                    is ContractNewsMain.State.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        setupNewsInLayout(
                            state.listNews,
                            mapViewOfNews,
                            sharedPreferencesManager.getSelectedTheme(SharedPreferencesManager.SELECTED_THEMES)
                        )
                    }
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.effect.collect {
                when (it) {
                    is ContractNewsMain.Effect.ShowToast -> {
                        Toast.makeText(
                            this@NewsMainFragment.context,
                            it.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }

    private fun inflateThemeCards() {

        val listOfTheme = sharedPreferencesManager
            .getSelectedTheme(SharedPreferencesManager.SELECTED_THEMES).toList()

        val linearLayout = binding.linearLayout as ViewGroup

        for (indexThemeOfNews in listOfTheme.indices) {
            val card = layoutInflater.inflate(R.layout.new_card, linearLayout, true) as ViewGroup
            val linearLayoutCard = card.getChildAt(indexThemeOfNews) as ViewGroup
            val textViewTheme = linearLayoutCard.getChildAt(0) as TextView
            textViewTheme.text = listOfTheme[indexThemeOfNews].toString()

            val cardView = linearLayoutCard.getChildAt(1) as ViewGroup
            val linearLayoutCardView = cardView.getChildAt(0) as ViewGroup

            for (indexOfNew in 0..COUNT_NEWS_IN_CARD_THEME) {
                val new = layoutInflater.inflate(
                    R.layout.new_view_item2,
                    linearLayoutCardView,
                    false
                )

                linearLayoutCardView.addView(new)
                val newViewGroup = new as LinearLayout
                mapViewOfNews[newViewGroup] = listOfTheme[indexThemeOfNews].toString()
            }
        }
    }

    private fun setupNewsInLayout(
        data: List<ArticleDb>,
        mapViewGroup: Map<ViewGroup, String>,
        setOfTheme: Set<MainNewsTheme>
    ) {
        setOfTheme.forEach {
            when (it) {
                MainNewsTheme.COUNTRY -> {
                    val filteredMap: Map<ViewGroup, String> = mapViewGroup.filterValues {
                        it == "country"
                    }
                    val filteredList: List<ArticleDb> = data.filter {
                        return@filter it.query == "country"
                    }
                    superViewOfNews(filteredList, filteredMap)
                }
                MainNewsTheme.FINANCE -> {
                    val filteredMap: Map<ViewGroup, String> = mapViewGroup.filterValues {
                        it == "finance"
                    }
                    val filteredList: List<ArticleDb> = data.filter {
                        return@filter it.query == "finance"
                    }
                    superViewOfNews(filteredList, filteredMap)
                }
                MainNewsTheme.HEALTH -> {
                    val filteredMap: Map<ViewGroup, String> = mapViewGroup.filterValues {
                        it == "health"
                    }
                    val filteredList: List<ArticleDb> = data.filter {
                        return@filter it.query == "health"
                    }
                    superViewOfNews(filteredList, filteredMap)
                }
                MainNewsTheme.POLICY -> {
                    val filteredMap: Map<ViewGroup, String> = mapViewGroup.filterValues {
                        it == "policy"
                    }
                    val filteredList: List<ArticleDb> = data.filter {
                        return@filter it.query == "policy"
                    }
                    superViewOfNews(filteredList, filteredMap)
                }
            }
        }
    }


    fun superViewOfNews(data: List<ArticleDb>, mapViewGroup: Map<ViewGroup, String>) {
        var indexListOfData = 0
        mapViewGroup.forEach {
            Log.i(TAG, "superViewOfNews: ${it.value}")
            val textViewAuthor = it.key.getChildAt(0) as TextView
            textViewAuthor.text = data[indexListOfData].author
            val textViewTitle = it.key.getChildAt(1) as TextView
            textViewTitle.text = data[indexListOfData].title
            indexListOfData++
        }
    }

    companion object {
        private const val TAG = "NewsMainFragment"
    }

}