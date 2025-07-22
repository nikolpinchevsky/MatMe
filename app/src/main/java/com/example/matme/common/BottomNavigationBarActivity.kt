package com.example.matme.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.matme.MainActivity
import com.example.matme.R
import com.example.matme.ui.FavoritesActivity
import com.example.matme.ui.ProfileActivity
import com.example.matme.ui.SearchActivity
import com.google.android.material.imageview.ShapeableImageView

open class BottomNavigationBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val currentActivity = this::class.java

        val homeButton = findViewById<ShapeableImageView>(R.id.iconHome)
        val searchButton = findViewById<ShapeableImageView>(R.id.iconSearch)
        val favoritesButton = findViewById<ShapeableImageView>(R.id.iconFavorites)
        val profileButton = findViewById<ShapeableImageView>(R.id.iconProfile)

        homeButton?.setOnClickListener {
            if (currentActivity != MainActivity::class.java) {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(0, 0)
                finish()
            }
        }

        searchButton?.setOnClickListener {
            if (currentActivity != SearchActivity::class.java) {
                startActivity(Intent(this, SearchActivity::class.java))
                overridePendingTransition(0, 0)
                finish()
            }
        }

        favoritesButton?.setOnClickListener {
            if (currentActivity != FavoritesActivity::class.java) {
                startActivity(Intent(this, FavoritesActivity::class.java))
                overridePendingTransition(0, 0)
                finish()
            }
        }

        profileButton?.setOnClickListener {
            if (currentActivity != ProfileActivity::class.java) {
                startActivity(Intent(this, ProfileActivity::class.java))
                overridePendingTransition(0, 0)
                finish()
            }
        }
    }
}

