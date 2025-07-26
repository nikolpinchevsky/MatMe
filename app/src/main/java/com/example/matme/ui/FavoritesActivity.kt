package com.example.matme.ui

import android.os.Bundle
import com.example.matme.adapters.FavoritesPagerAdapter
import com.example.matme.common.BottomNavigationBarActivity
import com.example.matme.databinding.FavoriteActivityBinding
import com.google.android.material.tabs.TabLayoutMediator

class FavoritesActivity : BottomNavigationBarActivity() {

    private lateinit var binding: FavoriteActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FavoriteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = FavoritesPagerAdapter(this)
        binding.viewPager.adapter = adapter

        val tabTitles = listOf("Favorites", "Custom Workouts")

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        setupBottomNavigation()
    }
}
