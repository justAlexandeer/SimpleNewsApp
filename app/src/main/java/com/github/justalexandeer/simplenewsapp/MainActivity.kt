package com.github.justalexandeer.simplenewsapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.justalexandeer.simplenewsapp.databinding.ActivityMainBinding
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.newsCacheFragment))
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

        binding.toolbar.setBackgroundColor(Color.BLUE)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}