package com.github.justalexandeer.simplenewsapp.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

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
            AppBarConfiguration(setOf(R.id.newsLineFragment, R.id.newsMainFragment))
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

        binding.toolbar.setBackgroundColor(Color.BLUE)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id == R.id.newsDetailFragment) {
                binding.toolbar.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
            } else {
                binding.toolbar.visibility = View.VISIBLE
                binding.bottomNav.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}