package com.example.matme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.example.matme.common.BottomNavigationBarActivity
import com.example.matme.ui.*
import com.example.matme.utils.DataSeeder
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

//Displays exercise categories and allows navigation to other activities
class MainActivity : BottomNavigationBarActivity() {

    private lateinit var auth: FirebaseAuth  // Firebase Authentication
    private lateinit var db: FirebaseDatabase  // Firebase Realtime Database
    private lateinit var userRef: DatabaseReference  // Reference to the logged-in user's data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        // Fill database with exercises if not already present
        DataSeeder.seedExercises()

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        // Check if user is logged in
        val user = auth.currentUser
        if (user == null) {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
            return
        }

        // Get a reference to the current user's data in the database
        userRef = db.getReference("users").child(user.uid)

        // Fetch and log the user's name from the database
        userRef.child("name").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.getValue(String::class.java)
                Log.d("FirebaseUser", "Value is: $name")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("FirebaseUser", "Failed to read value.", error.toException())
            }
        })

        // Set click listeners for category cards
        setupCategoryClicks()

        // Set click listener for the add workout button
        setupAddButton()
    }

    //Set click listeners for each exercise category
    private fun setupCategoryClicks() {
        findViewById<LinearLayout>(R.id.back).setOnClickListener {
            openExerciseList("back")
        }
        findViewById<LinearLayout>(R.id.arms).setOnClickListener {
            openExerciseList("arms")
        }
        findViewById<LinearLayout>(R.id.legs).setOnClickListener {
            openExerciseList("legs")
        }
        findViewById<LinearLayout>(R.id.chest).setOnClickListener {
            openExerciseList("chest")
        }
        findViewById<LinearLayout>(R.id.buttock).setOnClickListener {
            openExerciseList("buttock")
        }
        findViewById<LinearLayout>(R.id.stomach).setOnClickListener {
            openExerciseList("stomach")
        }
    }

    //Set click listener for "Add" icon to create a workout plan
    private fun setupAddButton() {
        findViewById<ShapeableImageView>(R.id.iconAdd)?.setOnClickListener {
            startActivity(Intent(this, CreateWorkoutPlanActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }

    //Open the Exercise List screen for the selected category
    private fun openExerciseList(category: String) {
        val intent = Intent(this, ExerciseListActivity::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }


}


