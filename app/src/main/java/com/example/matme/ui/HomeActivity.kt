package com.example.matme.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import com.example.matme.R
import com.example.matme.common.BottomNavigationBarActivity

// Home screen that displays muscle groups and opens related exercise lists
class HomeActivity : BottomNavigationBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout for the Home screen
        setContentView(R.layout.home_activity)

        // Find the layout elements for each muscle group
        val arms = findViewById<LinearLayout>(R.id.arms)
        val legs = findViewById<LinearLayout>(R.id.legs)
        val back = findViewById<LinearLayout>(R.id.back)
        val chest = findViewById<LinearLayout>(R.id.chest)
        val buttock = findViewById<LinearLayout>(R.id.buttock)
        val stomach = findViewById<LinearLayout>(R.id.stomach)

        // Set click listeners to open the exercise list for each category
        arms.setOnClickListener { openExerciseList("Arms") }
        legs.setOnClickListener { openExerciseList("Legs") }
        back.setOnClickListener { openExerciseList("Back") }
        chest.setOnClickListener { openExerciseList("Chest") }
        buttock.setOnClickListener { openExerciseList("Buttock") }
        stomach.setOnClickListener { openExerciseList("Stomach") }
    }

    // Opens the ExerciseListActivity and passes the selected category
    private fun openExerciseList(category: String) {
        val intent = Intent(this, ExerciseListActivity::class.java)
        intent.putExtra("CATEGORY", category)  // Send category name to next activity
        startActivity(intent)
    }
}