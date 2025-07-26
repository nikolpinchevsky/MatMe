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

class CustomWorkoutsFragment : Fragment() {

    private var _binding: FragmentCustomWorkoutsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        dbRef = FirebaseDatabase.getInstance().getReference("Plans")

        dbRef.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.layoutPlans.removeAllViews()
                    for (plan in snapshot.children) {
                        val name = plan.child("planName").getValue(String::class.java) ?: continue
                        val exercises = plan.child("exercises").children.mapNotNull { it.getValue(String::class.java) }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}