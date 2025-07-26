package com.example.matme.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.matme.common.BottomNavigationBarActivity
import com.example.matme.R
import com.example.matme.model.Exercise
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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

        loadExercises()

        createPlanButton.setOnClickListener {
            saveWorkoutPlan()
        }
    }

    private fun loadExercises() {
        val userId = auth.currentUser?.uid ?: return
        val favRef = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userId)
            .child("favorites")

        favRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(this@CreateWorkoutPlanActivity,
                        "No favorite exercises found",
                        Toast.LENGTH_SHORT).show()
                    return
                }

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


    private fun createExerciseView(name: String): View {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 8, 0, 8) }
            gravity = Gravity.CENTER_VERTICAL
            setPadding(16, 8, 16, 8)
        }

        val textView = MaterialTextView(this).apply {
            text = name
            textSize = 16f
            setTextColor(resources.getColor(android.R.color.black))
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

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

    private fun saveWorkoutPlan() {
        val planNameOriginal = editTextPlanName.text.toString().trim()
        val planNameLower = planNameOriginal.lowercase()
        val userId = auth.currentUser?.uid ?: return

        if (planNameOriginal.isEmpty() || selectedExercises.isEmpty()) {
            Toast.makeText(this,
                "Please enter a plan name and add at least one exercise",
                Toast.LENGTH_SHORT)
                .show()
            return
        }

        val dbRef = FirebaseDatabase.getInstance().getReference("Plans")
        val planId = dbRef.push().key ?: return

        val planData = mapOf(
            "planName" to planNameOriginal,
            "planNameLower" to planNameLower,
            "exercises" to selectedExercises.toList(),
            "userId" to userId
        )

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
