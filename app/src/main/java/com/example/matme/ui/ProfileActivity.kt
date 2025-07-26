package com.example.matme.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.matme.MainActivity
import com.example.matme.R
import com.example.matme.common.BottomNavigationBarActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileActivity : BottomNavigationBarActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var nameTextView: TextView
    private lateinit var textFavoritesCount: TextView
    private lateinit var textCustomCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        nameTextView = findViewById(R.id.textViewName)
        textFavoritesCount = findViewById(R.id.textFavoritesCount)
        textCustomCount = findViewById(R.id.textCustomCount)

        val logoutButton = findViewById<Button>(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        nameTextView.text = "Welcome"
        loadFavoritesCount()
        loadCustomWorkoutCount()
    }

    private fun loadFavoritesCount() {
        val userId = auth.currentUser?.uid ?: return
        db.child("users").child(userId).child("favorites")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.childrenCount
                    textFavoritesCount.text = count.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    textFavoritesCount.text = "0"
                }
            })
    }

    private fun loadCustomWorkoutCount() {
        val userId = auth.currentUser?.uid ?: return
        db.child("Plans")
            .orderByChild("userId")
            .equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.childrenCount
                    textCustomCount.text = count.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    textCustomCount.text = "0"
                }
            })
    }
}


