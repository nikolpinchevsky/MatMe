package com.example.matme.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.matme.common.BottomNavigationBarActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matme.R
import com.example.matme.model.Exercise
import com.example.matme.adapters.ExerciseAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity : BottomNavigationBarActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private val allExercises = mutableListOf<Exercise>()
    private var filteredExercises = mutableListOf<Exercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        searchEditText = findViewById(R.id.searchEditText)
        recyclerView = findViewById(R.id.exerciseRecyclerView)

        adapter = ExerciseAdapter(filteredExercises) { exercise ->
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val db = FirebaseDatabase.getInstance()
                val userFavoritesRef = db.collection("users").document(user.uid).collection("favorites")

                if (exercise.isFavorite) {
                    // Add to favorites
                    val data = mapOf("name" to exercise.name, "category" to exercise.category)
                    userFavoritesRef.document(exercise.name).set(data)
                } else {
                    // Remove from favorites
                    userFavoritesRef.document(exercise.name).delete()
                }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchAllExercises()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterExercises(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun fetchAllExercises() {
        val db = FirebaseDatabase.getInstance()
        db.collection("Exercises")
            .get()
            .addOnSuccessListener { result ->
                allExercises.clear()
                for (doc in result) {
                    val name = doc.getString("name") ?: continue
                    val category = doc.getString("category") ?: ""
                    allExercises.add(Exercise(name, category))
                }
                filterExercises(searchEditText.text.toString())
            }
    }

    private fun filterExercises(query: String) {
        val lowercaseQuery = query.lowercase()
        filteredExercises = allExercises.filter {
            it.name.lowercase().contains(lowercaseQuery)
        }.toMutableList()
        adapter = ExerciseAdapter(filteredExercises) { updatedExercise ->
            // Optional: handle favorite toggle here if needed
        }
        recyclerView.adapter = adapter
    }
}
