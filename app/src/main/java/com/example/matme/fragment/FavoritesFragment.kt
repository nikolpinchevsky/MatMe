package com.example.matme.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matme.adapters.ExerciseAdapter
import com.example.matme.databinding.FragmentFavoritesBinding
import com.example.matme.model.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// Displays a list of exercises that the user has marked as favorites, loaded from Firebase
class FavoritesFragment : Fragment() {

    // View binding for the fragment layout
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    // Adapter for displaying favorite exercises
    private lateinit var adapter: ExerciseAdapter

    // List to store the favorite exercises
    private val favoriteExercises = mutableListOf<Exercise>()

    // Inflate the fragment layout and initialize binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Called when the view is ready; sets up RecyclerView and loads favorites
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ExerciseAdapter(favoriteExercises, onFavoriteToggle = {})
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = adapter
        fetchFavorites()  // Load data from Firebase
    }

    // Retrieves the user's favorite exercises from Firebase
    private fun fetchFavorites() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseDatabase.getInstance().getReference("users")
            .child(uid)
            .child("favorites")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    favoriteExercises.clear()

                    // Convert each favorite name into an Exercise object
                    for (child in snapshot.children) {
                        child.key?.let {
                            favoriteExercises.add(Exercise(it, isFavorite = true))
                        }
                    }
                    // Update RecyclerView
                    adapter.updateList(favoriteExercises)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    // Cleans up binding to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
