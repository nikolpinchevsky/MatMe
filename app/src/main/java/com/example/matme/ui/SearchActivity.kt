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

class SearchActivity : BottomNavigationBarActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private lateinit var searchEditText: TextInputEditText
    private lateinit var dbRef: DatabaseReference
    private val allExercises = mutableListOf<Exercise>()
    private val filteredExercises = mutableListOf<Exercise>()
    private val favoriteNames = mutableSetOf<String>()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        recyclerView = findViewById(R.id.exerciseRecyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ExerciseAdapter(filteredExercises, ::toggleFavorite)

        recyclerView.adapter = adapter

        dbRef = FirebaseDatabase.getInstance().getReference("Exercises")

        fetchFavoritesThenLoadExercises()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterExercises(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

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
                    child.key?.let { favoriteNames.add(it) }
                }
                loadExercises()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun loadExercises() {
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allExercises.clear()
                for (group in snapshot.children) {
                    for (child in group.children) {
                        val exercise = child.getValue(Exercise::class.java)
                        if (exercise != null) {
                            exercise.isFavorite = favoriteNames.contains(exercise.name)
                            allExercises.add(exercise)
                        }
                    }
                }
                filterExercises(searchEditText.text.toString())
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun filterExercises(query: String) {
        val terms = query.trim().lowercase().split(" ")

        filteredExercises.clear()
        filteredExercises.addAll(allExercises.filter { exercise ->
            terms.all { term ->
                exercise.name.lowercase().contains(term)
            }
        })
        adapter.updateList(filteredExercises)
    }

    private fun toggleFavorite(exercise: Exercise) {
        val favRef = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userId ?: return)
            .child("favorites")
            .child(exercise.name)

        exercise.isFavorite = !exercise.isFavorite

        if (exercise.isFavorite) {
            favRef.setValue(true)
        } else {
            favRef.removeValue()
        }

        adapter.notifyDataSetChanged()
    }
}
