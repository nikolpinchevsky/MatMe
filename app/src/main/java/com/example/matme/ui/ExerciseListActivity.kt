package com.example.matme.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.matme.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ExerciseListActivity : AppCompatActivity() {

    private lateinit var db: FirebaseDatabase
    private lateinit var container: LinearLayout
    private val favoriteExercises = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_list_activity)

        val category = intent.getStringExtra("CATEGORY") ?: ""
        db = FirebaseDatabase.getInstance()
        container = findViewById(R.id.recyclerViewExercises)

        fetchExercises(category)
    }

    private fun fetchExercises(category: String) {
        db.collection("Exercises")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val name = document.getString("name") ?: continue
                    container.addView(createExerciseRow(name))
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load exercises", Toast.LENGTH_SHORT).show()
            }
    }

    private fun createExerciseRow(name: String): View {
        val rowLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
            setPadding(16, 12, 16, 12)
            gravity = Gravity.CENTER_VERTICAL
        }

        val textView = MaterialTextView(this).apply {
            text = name
            textSize = 18f
            setTextColor(resources.getColor(android.R.color.black))
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val heartIcon = ShapeableImageView(this).apply {
            setImageResource(R.drawable.red_heart_empty)
            layoutParams = LinearLayout.LayoutParams(48, 48)
            setPadding(8, 8, 8, 8)
            setOnClickListener {
                val isFavorite = favoriteExercises.contains(name)
                if (isFavorite) {
                    favoriteExercises.remove(name)
                    setImageResource(R.drawable.red_heart_empty)
                } else {
                    favoriteExercises.add(name)
                    setImageResource(R.drawable.red_heart_full)
                }
            }
        }

        rowLayout.addView(textView)
        rowLayout.addView(heartIcon)
        return rowLayout
    }
}
