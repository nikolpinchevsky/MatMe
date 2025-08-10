package com.example.matme.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.matme.R
import com.example.matme.common.BottomNavigationBarActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

//Activity to display user profile information, including name, favorites count, and custom workouts count
class ProfileActivity : BottomNavigationBarActivity() {
    private lateinit var auth: FirebaseAuth  // Firebase Authentication instance
    private lateinit var db: DatabaseReference  // Firebase Realtime Database reference
    private lateinit var nameTextView: TextView
    private lateinit var textFavoritesCount: TextView
    private lateinit var textCustomCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)  // Load profile layout

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        // Link UI elements
        nameTextView = findViewById(R.id.textViewName)
        textFavoritesCount = findViewById(R.id.textFavoritesCount)
        textCustomCount = findViewById(R.id.textCustomCount)

        // Set up logout button
        val logoutButton = findViewById<Button>(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)  // Redirect to login screen
            finish()
        }

        nameTextView.text = "Welcome"

        // Load counts for favorites and custom workouts from Firebase
        loadFavoritesCount()
        loadCustomWorkoutCount()
    }

    //Loads the number of favorite exercises for the logged-in user
    private fun loadFavoritesCount() {
        val userId = auth.currentUser?.uid ?: return
        db.child("users").child(userId).child("favorites")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.childrenCount  // Number of favorites
                    textFavoritesCount.text = count.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    textFavoritesCount.text = "0"  // If loading fails, show 0
                }
            })
    }

    //Loads the number of custom workout plans for the logged-in user
    private fun loadCustomWorkoutCount() {
        val userId = auth.currentUser?.uid ?: return
        db.child("Plans")
            .orderByChild("userId")
            .equalTo(userId)  // Get only the plans created by the current user
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.childrenCount  // Number of custom workouts
                    textCustomCount.text = count.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    textCustomCount.text = "0"
                }
            })
    }
}


