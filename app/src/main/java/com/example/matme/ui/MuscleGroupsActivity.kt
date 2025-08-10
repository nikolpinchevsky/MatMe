package com.example.matme.ui

import android.os.Bundle
import android.widget.Toast
import com.example.matme.common.BottomNavigationBarActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matme.R
import com.example.matme.model.Exercise
import com.google.firebase.database.*
import com.example.matme.adapters.ExerciseAdapter

//Activity to show exercises for a selected muscle group from Firebase

class MuscleGroupsActivity : BottomNavigationBarActivity() {

    private lateinit var dbRef: DatabaseReference  // Firebase database reference
    private lateinit var recyclerView: RecyclerView  // UI component for showing exercises
    private lateinit var adapter: ExerciseAdapter  // Adapter for the RecyclerView
    private val exerciseList = mutableListOf<Exercise>()  // List of exercises

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the layout that displays exercises
        setContentView(R.layout.exercise_list_activity)

        // Get the category from the Intent (e.g., "Arms", "Legs")
        val category = intent.getStringExtra("CATEGORY") ?: run {
            Toast.makeText(this,
                "Missing category",
                Toast.LENGTH_SHORT)
                .show()
            finish()
            return
        }

        // Set up RecyclerView with a vertical list layout
        recyclerView = findViewById(R.id.recyclerViewExercises)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with empty list and no favorite toggle logic

        adapter = ExerciseAdapter(exerciseList, onFavoriteToggle = {})
        recyclerView.adapter = adapter

        // Create a Firebase reference to "Exercises/<Category>"
        dbRef = FirebaseDatabase
            .getInstance()
            .getReference("Exercises")
            .child(category)

        // Load exercises from Firebase
        loadExercises()
    }

    // Loads exercises from Firebase and updates the RecyclerView
    private fun loadExercises() {
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                exerciseList.clear()
                for (child in snapshot.children) {
                    val exercise = child.getValue(Exercise::class.java)
                    exercise?.let { exerciseList.add(it) }  // Add valid exercises to list
                }
                adapter.updateList(exerciseList)  // Refresh RecyclerView
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MuscleGroupsActivity,
                    "Failed to load exercises",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}

