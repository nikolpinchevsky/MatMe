package com.example.matme.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import com.example.matme.R
import com.example.matme.common.BottomNavigationBarActivity

class HomeActivity : BottomNavigationBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val arms = findViewById<LinearLayout>(R.id.arms)
        val legs = findViewById<LinearLayout>(R.id.legs)
        val back = findViewById<LinearLayout>(R.id.back)
        val chest = findViewById<LinearLayout>(R.id.chest)
        val buttock = findViewById<LinearLayout>(R.id.buttock)
        val stomach = findViewById<LinearLayout>(R.id.stomach)

        arms.setOnClickListener { openExerciseList("Arms") }
        legs.setOnClickListener { openExerciseList("Legs") }
        back.setOnClickListener { openExerciseList("Back") }
        chest.setOnClickListener { openExerciseList("Chest") }
        buttock.setOnClickListener { openExerciseList("Buttock") }
        stomach.setOnClickListener { openExerciseList("Stomach") }
    }

    private fun openExerciseList(category: String) {
        val intent = Intent(this, ExerciseListActivity::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }
}