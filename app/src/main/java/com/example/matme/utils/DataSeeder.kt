package com.example.matme.utils

import com.example.matme.model.Exercise
import com.google.firebase.database.FirebaseDatabase

object DataSeeder {

    fun seedExercises() {
        val database = FirebaseDatabase.getInstance().reference.child("Exercises")

        val exercisesMap = mapOf(
            "Back" to listOf(
                Exercise("Lat Pulldown"),
                Exercise("Pull-Up"),
                Exercise("Deadlift"),
                Exercise("Bent-over Row"),
                Exercise("T-Bar Row"),
                Exercise("Seated Cable Row"),
                Exercise("Straight Arm Pulldown"),
                Exercise("Superman")
            ),
            "Arms" to listOf(
                Exercise("Bicep Curl"),
                Exercise("Hammer Curl"),
                Exercise("Tricep Pushdown"),
                Exercise("Overhead Tricep Extension"),
                Exercise("Preacher Curl"),
                Exercise("Tricep Dips"),
                Exercise("Zottman Curl"),
                Exercise("Concentration Curl")
            ),
            "Legs" to listOf(
                Exercise("Squat"),
                Exercise("Leg Press"),
                Exercise("Lunge"),
                Exercise("Leg Curl"),
                Exercise("Leg Extension"),
                Exercise("Calf Raise"),
                Exercise("Step Up"),
                Exercise("Bulgarian Split Squat")
            ),
            "Chest" to listOf(
                Exercise("Bench Press"),
                Exercise("Incline Press"),
                Exercise("Chest Fly"),
                Exercise("Push-up"),
                Exercise("Cable Crossover"),
                Exercise("Decline Press"),
                Exercise("Dips"),
                Exercise("Pec Deck")
            ),
            "Buttock" to listOf(
                Exercise("Hip Thrust"),
                Exercise("Glute Kickback"),
                Exercise("Sumo Squat"),
                Exercise("Step-ups"),
                Exercise("Bulgarian Split Squat"),
                Exercise("Romanian Deadlift"),
                Exercise("Fire Hydrant"),
                Exercise("Glute Bridge")
            ),
            "Stomach" to listOf(
                Exercise("Crunch"),
                Exercise("Leg Raise"),
                Exercise("Plank"),
                Exercise("Russian Twist"),
                Exercise("Bicycle Crunch"),
                Exercise("Mountain Climbers"),
                Exercise("Flutter Kicks"),
                Exercise("Hanging Knee Raise")
            )
        )

        for ((category, exercises) in exercisesMap) {
            database.child(category).setValue(exercises)
        }
    }
}
