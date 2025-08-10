package com.example.matme.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.matme.MainActivity
import com.example.matme.R
import com.example.matme.ui.CreateWorkoutPlanActivity
import com.example.matme.ui.FavoritesActivity
import com.example.matme.ui.ProfileActivity
import com.example.matme.ui.SearchActivity
import com.google.android.material.imageview.ShapeableImageView

// Base Activity for Bottom Navigation Bar
// // Handles navigation between main sections of the app
open class BottomNavigationBarActivity : AppCompatActivity() {

    // Called when the Activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Ensures bottom navigation is set up every time a layout is set
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupBottomNavigation()
    }

    // Sets up click listeners for bottom navigation icons
    protected fun setupBottomNavigation() {
        val currentActivity = this::class.java

        val homeButton = findViewById<ShapeableImageView>(R.id.iconHome)
        val searchButton = findViewById<ShapeableImageView>(R.id.iconSearch)
        val addButton = findViewById<ShapeableImageView>(R.id.iconAdd)
        val favoritesButton = findViewById<ShapeableImageView>(R.id.iconFavorites)
        val profileButton = findViewById<ShapeableImageView>(R.id.iconProfile)

        // Go to Home (MainActivity) if not already there
        homeButton?.setOnClickListener {
            if (currentActivity != MainActivity::class.java) {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(0, 0)
            }
        }

        // Go to SearchActivity
        searchButton?.setOnClickListener {
            if (currentActivity != SearchActivity::class.java) {
                startActivity(Intent(this, SearchActivity::class.java))
                overridePendingTransition(0, 0)
            }
        }

        // Go to CreateWorkoutPlanActivity
        addButton?.setOnClickListener {
            if (currentActivity != CreateWorkoutPlanActivity::class.java) {
                startActivity(Intent(this, CreateWorkoutPlanActivity::class.java))
                overridePendingTransition(0, 0)
            }
        }

        // Go to FavoritesActivity
        favoritesButton?.setOnClickListener {
            if (currentActivity != FavoritesActivity::class.java) {
                startActivity(Intent(this, FavoritesActivity::class.java))
                overridePendingTransition(0, 0)
            }
        }

        // Go to ProfileActivity
        profileButton?.setOnClickListener {
            if (currentActivity != ProfileActivity::class.java) {
                startActivity(Intent(this, ProfileActivity::class.java))
                overridePendingTransition(0, 0)
            }
        }
    }
}


