package com.example.matme.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.matme.R
import com.example.matme.common.BottomNavigationBarActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class MuscleGroupsActivity : BottomNavigationBarActivity() {
    private lateinit var recyclerViewExercises: ListView
    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        recyclerViewExercises = findViewById(R.id.recyclerViewExercises)
        db = FirebaseDatabase.getInstance()

        val muscleGroup = intent.getStringExtra("muscleGroup")

        if (muscleGroup != null) {
            fetchExercises(muscleGroup)
        }
    }

    private fun fetchExercises(muscleGroup: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("exercises").child(muscleGroup)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val exercises = mutableListOf<String>()
                for (child in snapshot.children) {
                    val exercise = child.getValue(String::class.java)
                    if (!exercise.isNullOrBlank()) {
                        exercises.add(exercise)
                    }
                }

                val adapter = ArrayAdapter(this@MuscleGroupsActivity, android.R.layout.simple_list_item_1, exercises)
                recyclerViewExercises.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MuscleGroupsActivity, "Failed to load exercises", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
