package com.example.matme.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matme.fragment.CustomWorkoutsFragment
import com.example.matme.fragment.FavoritesFragment

//Handles switching between FavoritesFragment and CustomWorkoutsFragment
class FavoritesPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // Returns the number of pages/tabs
    override fun getItemCount(): Int = 2

    //Creates and returns the correct Fragment for each tab position
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoritesFragment()  // First tab → Favorites list
            1 -> CustomWorkoutsFragment()  // Second tab → Custom workout plans
            else -> throw IllegalStateException("Invalid position")
        }
    }
}