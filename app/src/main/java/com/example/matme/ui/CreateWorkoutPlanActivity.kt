package com.example.matme.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.matme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class CreateWorkoutPlanActivity : AppCompatActivity() {

    private lateinit var editTextPlanName: EditText
    private lateinit var exerciseListContainer: LinearLayout
    private lateinit var buttonCreatePlan: Button

    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private val allExercises = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_workout_plan_activity)

        db = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        editTextPlanName = findViewById(R.id.editTextPlanName)
        exerciseListContainer = findViewById(R.id.exerciseListContainer)
        buttonCreatePlan = findViewById(R.id.buttonCreatePlan)

        displayExercises()
        fetchExercisesFromDatabase()

        buttonCreatePlan.setOnClickListener {
            saveWorkoutPlan()
        }
    }

    private fun fetchExercisesFromDatabase() {
        val dbRef = FirebaseDatabase.getInstance().getReference("Exercises")

        dbRef.get().addOnSuccessListener { snapshot ->
            allExercises.clear()
            for (child in snapshot.children) {
                val name = child.child("name").getValue(String::class.java)
                if (!name.isNullOrBlank()) {
                    allExercises.add(name)
                }
            }
            displayExercises()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load exercises", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayExercises() {
        allExercises.forEach { exercise ->
            val checkBox = CheckBox(this)
            checkBox.text = exercise
            checkBox.textSize = 16f
            exerciseListContainer.addView(checkBox)
        }
    }

    private fun saveWorkoutPlan() {
        val planName = editTextPlanName.text.toString().trim()
        val selectedExercises = mutableListOf<String>()

        for (i in 0 until exerciseListContainer.childCount) {
            val view = exerciseListContainer.getChildAt(i)
            if (view is CheckBox && view.isChecked) {
                selectedExercises.add(view.text.toString())
            }
        }

        if (planName.isEmpty() || selectedExercises.isEmpty()) {
            Toast.makeText(this, "Please enter a plan name and select exercises", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid ?: return

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("WorkoutPlans")

        val planId = myRef.push().key

        val planData = mapOf(
            "planName" to planName,
            "exercises" to selectedExercises,
            "userId" to userId
        )

        if (planId != null) {
            myRef.child(planId).setValue(planData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Workout plan saved successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save plan", Toast.LENGTH_SHORT).show()
                }
        }
    }

}
