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

class MuscleGroupsActivity : BottomNavigationBarActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private val exerciseList = mutableListOf<Exercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_list_activity)

        val category = intent.getStringExtra("CATEGORY") ?: run {
            Toast.makeText(this,
                "Missing category",
                Toast.LENGTH_SHORT)
                .show()
            finish()
            return
        }

        recyclerView = findViewById(R.id.recyclerViewExercises)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExerciseAdapter(exerciseList, onFavoriteToggle = {})
        recyclerView.adapter = adapter

        dbRef = FirebaseDatabase
            .getInstance()
            .getReference("Exercises")
            .child(category)
        loadExercises()
    }

    private fun loadExercises() {
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                exerciseList.clear()
                for (child in snapshot.children) {
                    val exercise = child.getValue(Exercise::class.java)
                    exercise?.let { exerciseList.add(it) }
                }
                adapter.updateList(exerciseList)
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

