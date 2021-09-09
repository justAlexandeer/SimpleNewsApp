package com.github.justalexandeer.simplenewsapp.ui.walkthrough

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.justalexandeer.simplenewsapp.data.SharedPreferencesManager
import com.github.justalexandeer.simplenewsapp.databinding.ActivityWalkthroughBinding
import com.github.justalexandeer.simplenewsapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WalkthroughActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalkthroughBinding
    private val viewModel: WalkthroughViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkthroughBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.checkIsFirstStart()
        binding.button.setOnClickListener {
            onMainActivity()
        }
    }



    private fun onMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }



    companion object {
        private const val TAG = "WalkthroughActivity"
    }

}