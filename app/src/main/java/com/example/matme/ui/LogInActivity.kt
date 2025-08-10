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

//Activity for logging in users with email and password (Firebase Authentication)
class LogInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the login screen layout
        setContentView(R.layout.log_in_activity)

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance()

        // If a user is already logged in, go directly to the main screen
        if (auth.currentUser != null) {
            navigateToMainActivity()
        }

        findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextEmail).text.toString().trim()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString().trim()
            loginWithEmailPassword(email, password)
        }

        findViewById<Button>(R.id.buttonSignIn).setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }


    // Logs in the user with the provided email and password
    private fun loginWithEmailPassword(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,
                "Please enter both email and password",
                Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Try to log in using Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // Login success → go to main screen
                if (task.isSuccessful) {
                    navigateToMainActivity()
                } else {
                 // Show error if login fails
                    Toast.makeText(this,
                        "Email login failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    // Opens the MainActivity and closes the login screen
    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
