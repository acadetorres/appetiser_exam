package com.acdetorres.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.acdetorres.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavHost()
    }

    private fun setupNavHost() {
        val navController = findNavController(R.id.navHostMain)
        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_main)

        navController.graph = navGraph
    }

    fun showLoading(isLoading : Boolean) {

    }
}