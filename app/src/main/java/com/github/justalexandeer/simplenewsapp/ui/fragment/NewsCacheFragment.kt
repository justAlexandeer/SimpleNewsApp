package com.github.justalexandeer.simplenewsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import com.github.justalexandeer.simplenewsapp.api.NewsApi
import com.github.justalexandeer.simplenewsapp.data.models.SuccessResponse
import com.github.justalexandeer.simplenewsapp.databinding.FragmentNewsCacheBinding
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

@AndroidEntryPoint
class NewsCacheFragment : Fragment() {

    private lateinit var binding: FragmentNewsCacheBinding
    @Inject lateinit var newsApi: NewsApi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsCacheBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            val response = newsApi.getTest("butcoin", MainRepository.apiKey, 10, 10)
            response.enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: retrofit2.Response<SuccessResponse>
                ) {
                    Log.i(TAG, "onResponse: SUCCESS")
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    Log.i(TAG, "onFailure: FAIL")
                }
            })
        }
    }

    companion object {
        private const val TAG = "NewsCacheFragment"
    }

}