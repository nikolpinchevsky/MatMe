package com.example.matme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matme.R
import com.example.matme.model.Exercise


class ExerciseAdapter(
    private val exercises: List<Exercise>,
    private val onFavoriteToggle: (Exercise) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.exerciseName)
        val heartIcon: ImageView = itemView.findViewById(R.id.heartIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.nameTextView.text = exercise.name

        holder.heartIcon.setImageResource(
            if (exercise.isFavorite) R.drawable.red_heart_full else R.drawable.red_heart_empty
        )

        holder.heartIcon.setOnClickListener {
            exercise.isFavorite = !exercise.isFavorite
            notifyItemChanged(position)
            onFavoriteToggle(exercise)
        }
    }

    override fun getItemCount() = exercises.size
}
