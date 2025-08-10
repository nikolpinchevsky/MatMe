package com.example.matme.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.matme.MainActivity
import com.example.matme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

//Activity for user registration (Sign Up)
class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth  // Firebase Authentication instance
    private lateinit var db: FirebaseDatabase   // Firebase Realtime Database instance
    private lateinit var nameEditText: EditText  // User name input
    private lateinit var emailEditText: EditText  // Email input
    private lateinit var passwordEditText: EditText  // Password input
    private lateinit var signInButton: Button  // Sign up button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_activity)

        // Initialize Firebase services
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        // Link UI elements
        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        signInButton = findViewById(R.id.buttonSignIn)

        // When user clicks Sign Up button
        signInButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,
                    "All fields must be filled in.",
                    Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Create a new user in Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                        // Store user info in the database
                        val userMap = mapOf(
                            "name" to name,
                            "email" to email
                        )

                        db.getReference("users").child(userId).setValue(userMap)
                            .addOnSuccessListener {
                                // Navigate to MainActivity after successful registration
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this,
                                    "Error saving user info: ${e.message}",
                                    Toast.LENGTH_SHORT)
                                    .show()
                            }

                    } else {
                        // Show error if registration fails
                        Toast.makeText(this,
                            "Error: ${task.exception?.message}",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}
