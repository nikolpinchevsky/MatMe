package com.example.matme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matme.R
import com.example.matme.model.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// Handles showing each exercise name and its favorite status
class ExerciseAdapter(
    private var exercises: List<Exercise>,  // List of exercises to display
    private val onFavoriteToggle: (Exercise) -> Unit  // Callback when favorite is toggled
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid  // Current logged-in user ID
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")  // Firebase reference to "users"

    //Creates a new ViewHolder for an exercise item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    //Binds data from an exercise to the ViewHolder
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    //Returns the total number of exercises
    override fun getItemCount(): Int = exercises.size

    //Updates the list of exercises and refreshes the RecyclerView
    fun updateList(newList: List<Exercise>) {
        exercises = newList
        notifyDataSetChanged()
    }

    // Inner class for holding and binding exercise item views
    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.exerciseName)
        private val favoriteIcon: ImageView = itemView.findViewById(R.id.heartIcon)

        //Binds exercise data to UI components
        fun bind(exercise: Exercise) {
            // Set correct heart icon based on favorite status
            nameText.text = exercise.name
            favoriteIcon.setImageResource(
                if (exercise.isFavorite) R.drawable.red_heart_full else R.drawable.red_heart_empty
            )

            // Handle click on favorite icon
            favoriteIcon.setOnClickListener {
                val newState = !exercise.isFavorite
                exercise.isFavorite = newState

                // Update Firebase favorite data
                userId?.let { uid ->
                    val favRef = dbRef.child(uid).child("favorites").child(exercise.name)
                    if (newState) {
                        favRef.setValue(true)
                    } else {
                        favRef.removeValue()
                    }
                }

                // Update UI
                favoriteIcon.setImageResource(
                    if (newState) R.drawable.red_heart_full else R.drawable.red_heart_empty
                )
            }
        }
    }
}


