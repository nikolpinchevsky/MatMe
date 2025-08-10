package com.example.matme.ui

import android.os.Bundle
import com.example.matme.adapters.FavoritesPagerAdapter
import com.example.matme.common.BottomNavigationBarActivity
import com.example.matme.databinding.FavoriteActivityBinding
import com.google.android.material.tabs.TabLayoutMediator

// Activity to display Favorites and Custom Workouts in a tab layout
class FavoritesActivity : BottomNavigationBarActivity() {

    private lateinit var binding: FavoriteActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = FavoriteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up ViewPager with FavoritesPagerAdapter (handles switching between tabs)
        val adapter = FavoritesPagerAdapter(this)
        binding.viewPager.adapter = adapter

        val tabTitles = listOf("Favorites", "Custom Workouts")

        // Link the TabLayout and ViewPager so tabs show correct titles and content
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        // Set up the bottom navigation bar from parent class
        setupBottomNavigation()
    }
}
