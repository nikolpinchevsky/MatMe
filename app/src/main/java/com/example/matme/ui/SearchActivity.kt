package com.example.matme.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matme.R
import com.example.matme.common.BottomNavigationBarActivity
import com.example.matme.model.Exercise
import com.example.matme.adapters.ExerciseAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

//Activity that allows the user to search exercises and mark them as favorites
class SearchActivity : BottomNavigationBarActivity() {

    private lateinit var recyclerView: RecyclerView  // List to display exercises
    private lateinit var adapter: ExerciseAdapter  // Adapter for displaying exercises
    private lateinit var searchEditText: TextInputEditText  // Search input field
    private lateinit var dbRef: DatabaseReference  // Firebase reference for exercises
    private val allExercises = mutableListOf<Exercise>()  // All exercises from the database
    private val filteredExercises = mutableListOf<Exercise>()  // Filtered exercises based on search
    private val favoriteNames = mutableSetOf<String>()  // Names of user's favorite exercises
    private val userId = FirebaseAuth.getInstance().currentUser?.uid  // Current logged-in user ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        // Link UI elements
        recyclerView = findViewById(R.id.exerciseRecyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up adapter with favorite toggle function
        adapter = ExerciseAdapter(filteredExercises, ::toggleFavorite)
        recyclerView.adapter = adapter

        // Firebase reference to "Exercises"
        dbRef = FirebaseDatabase.getInstance().getReference("Exercises")

        // Load user's favorites first, then load all exercises
        fetchFavoritesThenLoadExercises()

        // Listen for text changes to filter exercises in real-time
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterExercises(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    //First fetch user's favorites from Firebase, then load exercises
    private fun fetchFavoritesThenLoadExercises() {
        if (userId == null) return

        val favRef = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userId)
            .child("favorites")

        favRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favoriteNames.clear()
                for (child in snapshot.children) {
                    child.key?.let { favoriteNames.add(it) }  // Store each favorite's name
                }
                loadExercises()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    //Loads all exercises from Firebase and marks favorites
    private fun loadExercises() {
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allExercises.clear()
                // Iterate over categories
                for (group in snapshot.children) {
                    // Iterate over exercises
                    for (child in group.children) {
                        val exercise = child.getValue(Exercise::class.java)
                        if (exercise != null) {
                            exercise.isFavorite = favoriteNames.contains(exercise.name)
                            allExercises.add(exercise)
                        }
                    }
                }
                // Apply current search filter
                filterExercises(searchEditText.text.toString())
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    //Filters exercises based on search text
    private fun filterExercises(query: String) {
        val terms = query.trim().lowercase().split(" ")

        filteredExercises.clear()
        filteredExercises.addAll(allExercises.filter { exercise ->
            terms.all { term ->
                exercise.name.lowercase().contains(term)
            }
        })
        // Refresh list
        adapter.updateList(filteredExercises)
    }

    //Toggles an exercise as favorite or removes it from favorites
    private fun toggleFavorite(exercise: Exercise) {
        val favRef = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userId ?: return)
            .child("favorites")
            .child(exercise.name)

        // Switch favorite state
        exercise.isFavorite = !exercise.isFavorite

        // Add to favorites
        if (exercise.isFavorite) {
            favRef.setValue(true)
        } else {
            // Remove from favorites
            favRef.removeValue()
        }
        // Update UI
        adapter.notifyDataSetChanged()
    }
}
