package com.example.matme.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matme.fragment.CustomWorkoutsFragment
import com.example.matme.fragment.FavoritesFragment

class FavoritesPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoritesFragment()
            1 -> CustomWorkoutsFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}