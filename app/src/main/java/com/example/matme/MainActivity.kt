package com.example.matme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.example.matme.common.BottomNavigationBarActivity
import com.example.matme.ui.*
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : BottomNavigationBarActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        val user = auth.currentUser
        if (user == null) {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
            return
        }

        userRef = db.getReference("users").child(user.uid)

        userRef.child("name").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.getValue(String::class.java)
                Toast.makeText(this@MainActivity, "Welcome ${name ?: ""}", Toast.LENGTH_SHORT).show()
                Log.d("FirebaseUser", "Value is: $name")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("FirebaseUser", "Failed to read value.", error.toException())
            }
        })

        setupCategoryClicks()
        setupAddButton()
    }

    private fun setupCategoryClicks() {
        findViewById<LinearLayout>(R.id.back).setOnClickListener {
            openExerciseList("back")
        }
        findViewById<LinearLayout>(R.id.arms).setOnClickListener {
            openExerciseList("arms")
        }
        findViewById<LinearLayout>(R.id.legs).setOnClickListener {
            openExerciseList("legs")
        }
        findViewById<LinearLayout>(R.id.chest).setOnClickListener {
            openExerciseList("chest")
        }
        findViewById<LinearLayout>(R.id.buttock).setOnClickListener {
            openExerciseList("buttock")
        }
        findViewById<LinearLayout>(R.id.stomach).setOnClickListener {
            openExerciseList("stomach")
        }
    }

    private fun setupAddButton() {
        findViewById<ShapeableImageView>(R.id.iconAdd)?.setOnClickListener {
            startActivity(Intent(this, CreateWorkoutPlanActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }
    }

    private fun openExerciseList(category: String) {
        val intent = Intent(this, ExerciseListActivity::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }
}


