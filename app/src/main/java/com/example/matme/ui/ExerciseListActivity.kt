package com.example.matme.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.matme.common.BottomNavigationBarActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matme.R
import com.example.matme.model.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.matme.adapters.ExerciseAdapter

class ExerciseListActivity : BottomNavigationBarActivity() {
    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private val favoriteExercises = mutableSetOf<String>()
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_list_activity)

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid
        db = FirebaseDatabase.getInstance()

        recyclerView = findViewById(R.id.recyclerViewExercises)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExerciseAdapter(emptyList()) { exercise ->
            toggleFavorite(exercise)
        }
        recyclerView.adapter = adapter

        val category = intent.getStringExtra("CATEGORY")?.replaceFirstChar { it.uppercase() } ?: ""

        Log.d("CATEGORY_CHECK", "Category received: $category")

        loadFavorites{
            fetchExercises(category)
        }
    }

    private fun loadFavorites(onComplete: () -> Unit) {
        userId?.let { uid ->
            val favRef = db.getReference("users")
                .child(uid)
                .child("favorites")
            favRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        child.key?.let { favoriteExercises.add(it) }
                    }
                    onComplete()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ExerciseListActivity,
                        "Failed to load favorites",
                        Toast.LENGTH_SHORT)
                        .show()
                    onComplete()
                }
            })
        } ?: onComplete()
    }

    private fun fetchExercises(category: String) {
        val dbRef = db.getReference("Exercises").child(category)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val exercises = mutableListOf<Exercise>()
                for (child in snapshot.children) {
                    val exercise = child.getValue(Exercise::class.java)
                    exercise?.let {
                        it.isFavorite = favoriteExercises.contains(it.name)
                        exercises.add(it)
                    }
                }
                adapter.updateList(exercises)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ExerciseListActivity,
                    "Failed to load exercises",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun toggleFavorite(exercise: Exercise) {
        val name = exercise.name
        val userFavoritesRef = db.getReference("users")
            .child(userId!!)
            .child("favorites")

        if (favoriteExercises.contains(name)) {
            userFavoritesRef.child(name).removeValue()
            favoriteExercises.remove(name)
        } else {
            userFavoritesRef.child(name).setValue(true)
            favoriteExercises.add(name)
        }
        exercise.isFavorite = favoriteExercises.contains(name)
        adapter.notifyDataSetChanged()
    }
}

