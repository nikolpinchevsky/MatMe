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

class ExerciseAdapter(
    private var exercises: List<Exercise>,
    private val onFavoriteToggle: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size

    fun updateList(newList: List<Exercise>) {
        exercises = newList
        notifyDataSetChanged()
    }

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.exerciseName)
        private val favoriteIcon: ImageView = itemView.findViewById(R.id.heartIcon)

        fun bind(exercise: Exercise) {
            nameText.text = exercise.name
            favoriteIcon.setImageResource(
                if (exercise.isFavorite) R.drawable.red_heart_full else R.drawable.red_heart_empty
            )

            favoriteIcon.setOnClickListener {
                val newState = !exercise.isFavorite
                exercise.isFavorite = newState

                // Update Firebase
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


