package com.example.matme.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.matme.common.BottomNavigationBarActivity
import com.example.matme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : BottomNavigationBarActivity(){
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    private lateinit var nameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        nameTextView = findViewById(R.id.textViewName)

        val user = auth.currentUser
        if (user != null) {
            val uid = user.uid

            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name")
                        nameTextView.text = "Welcome, $name"
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error loading user info", Toast.LENGTH_SHORT).show()
                }
        }
    }


}
