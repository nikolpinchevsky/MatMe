package com.example.matme.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.matme.common.BottomNavigationBarActivity
import com.example.matme.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// Activity extends BottomNavigationBarActivity to reuse the bottom nav UI.
class CreateWorkoutPlanActivity : BottomNavigationBarActivity() {
    private lateinit var editTextPlanName: TextInputEditText
    private lateinit var createPlanButton: MaterialButton
    private lateinit var exerciseListContainer: LinearLayout
    private lateinit var auth: FirebaseAuth
    private val selectedExercises = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_workout_plan_activity)

        editTextPlanName = findViewById(R.id.editTextPlanName)
        createPlanButton = findViewById(R.id.buttonCreatePlan)
        exerciseListContainer = findViewById(R.id.exerciseListContainer)
        auth = FirebaseAuth.getInstance()

        // Load the user's favorite exercises from Firebase
        loadExercises()

        // Save plan when the "Create Plan" button is clicked
        createPlanButton.setOnClickListener {
            saveWorkoutPlan()
        }
    }

    // Loads the user's favorite exercises from Firebase and shows them in the list
    private fun loadExercises() {
        // Get current user ID or exit if not logged in
        val userId = auth.currentUser?.uid ?: return
        // Reference to user's favorites in Firebase
        val favRef = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userId)
            .child("favorites")

        // Read favorites once
        favRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(this@CreateWorkoutPlanActivity,
                        "No favorite exercises found",
                        Toast.LENGTH_SHORT).show()
                    return
                }

                // For each favorite exercise, create a row in the UI
                for (child in snapshot.children) {
                    val exerciseName = child.key ?: continue
                    exerciseListContainer.addView(createExerciseView(exerciseName))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CreateWorkoutPlanActivity,
                    "Failed to load favorite exercises",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Creates a single row in the list with the exercise name and a "+" button to add it to the plan
    private fun createExerciseView(name: String): View {
        // Creates a horizontal row with the exercise name and a "+" button
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 8, 0, 8) }
            gravity = Gravity.CENTER_VERTICAL
            setPadding(16, 8, 16, 8)
        }

        // Text displaying the exercise name
        val textView = MaterialTextView(this).apply {
            text = name
            textSize = 16f
            setTextColor(resources.getColor(android.R.color.black))
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        // Button to add exercise to selected list
        val addButton = MaterialButton(this).apply {
            text = "+"
            setOnClickListener {
                if (selectedExercises.contains(name)) {
                    Toast.makeText(this@CreateWorkoutPlanActivity,
                        "$name is already added",
                        Toast.LENGTH_SHORT)
                        .show()
                } else {
                    selectedExercises.add(name)
                    Toast.makeText(this@CreateWorkoutPlanActivity,
                        "$name added to the plan",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        row.addView(textView)
        row.addView(addButton)
        return row
    }

    // Saves the workout plan with its name and selected exercises to Firebase
    private fun saveWorkoutPlan() {
        val planNameOriginal = editTextPlanName.text.toString().trim()
        val planNameLower = planNameOriginal.lowercase()
        val userId = auth.currentUser?.uid ?: return

        // Validation: name and exercises must be provided
        if (planNameOriginal.isEmpty() || selectedExercises.isEmpty()) {
            Toast.makeText(this,
                "Please enter a plan name and add at least one exercise",
                Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Create new plan in "Plans" collection in Firebase
        val dbRef = FirebaseDatabase.getInstance().getReference("Plans")
        val planId = dbRef.push().key ?: return

        val planData = mapOf(
            "planName" to planNameOriginal,
            "planNameLower" to planNameLower,
            "exercises" to selectedExercises.toList(),
            "userId" to userId
        )

        // Save plan and give user feedback
        dbRef.child(planId).setValue(planData)
            .addOnSuccessListener {
                Toast.makeText(this,
                    "Workout plan saved successfully!",
                    Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this,
                    "Failed to save the plan",
                    Toast.LENGTH_SHORT)
                    .show()
            }
    }
}
