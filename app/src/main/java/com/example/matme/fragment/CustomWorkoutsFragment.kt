package com.example.matme.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.matme.databinding.FragmentCustomWorkoutsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// Displays the user's saved custom workout plans from Firebase
class CustomWorkoutsFragment : Fragment() {

    // View binding for the fragment
    private var _binding: FragmentCustomWorkoutsBinding? = null
    private val binding get() = _binding!!

    // Firebase Database reference
    private lateinit var dbRef: DatabaseReference

    // Inflates the layout for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Called when the view is fully created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        dbRef = FirebaseDatabase.getInstance().getReference("Plans")

        // Load only the plans of the current logged-in user
        dbRef.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.layoutPlans.removeAllViews()  // Clear previous list
                    for (plan in snapshot.children) {
                        val name = plan.child("planName").getValue(String::class.java) ?: continue
                        val exercises = plan.child("exercises").children.mapNotNull { it.getValue(String::class.java) }

                        // Create a TextView for each plan
                        val textView = TextView(requireContext()).apply {
                            text = "$name:\n- ${exercises.joinToString("\n- ")}"
                            textSize = 16f
                            setPadding(16, 16, 16, 32)
                        }
                        binding.layoutPlans.addView(textView)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    // Cleans up the binding to prevent memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}