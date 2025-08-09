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
                Exercise("Superman"),
                Exercise("Single-Arm Dumbbell Row"),
                Exercise("Inverted Row"),
                Exercise("Good Morning"),
                Exercise("Reverse Fly"),
                Exercise("Back Extension"),
                Exercise("Meadows Row"),
                Exercise("Kroc Row"),
                Exercise("Seal Row")
            ),
            "Arms" to listOf(
                Exercise("Bicep Curl"),
                Exercise("Hammer Curl"),
                Exercise("Tricep Pushdown"),
                Exercise("Overhead Tricep Extension"),
                Exercise("Preacher Curl"),
                Exercise("Tricep Dips"),
                Exercise("Zottman Curl"),
                Exercise("Concentration Curl"),
                Exercise("Barbell Curl"),
                Exercise("EZ-Bar Curl"),
                Exercise("Cable Curl"),
                Exercise("Reverse Curl"),
                Exercise("Skull Crushers"),
                Exercise("Close-Grip Bench Press"),
                Exercise("Triceps Kickback"),
                Exercise("Cable Rope Overhead Extension")
            ),
            "Legs" to listOf(
                Exercise("Squat"),
                Exercise("Leg Press"),
                Exercise("Lunge"),
                Exercise("Leg Curl"),
                Exercise("Leg Extension"),
                Exercise("Calf Raise"),
                Exercise("Step Up"),
                Exercise("Bulgarian Split Squat"),
                Exercise("Front Squat"),
                Exercise("Goblet Squat"),
                Exercise("Hack Squat"),
                Exercise("Sumo Deadlift"),
                Exercise("Walking Lunge"),
                Exercise("Nordic Hamstring Curl"),
                Exercise("Box Jump"),
                Exercise("Hip Adduction Machine")
            ),
            "Chest" to listOf(
                Exercise("Bench Press"),
                Exercise("Incline Press"),
                Exercise("Chest Fly"),
                Exercise("Push-up"),
                Exercise("Cable Crossover"),
                Exercise("Decline Press"),
                Exercise("Dips"),
                Exercise("Pec Deck"),
                Exercise("Dumbbell Bench Press"),
                Exercise("Dumbbell Incline Press"),
                Exercise("Machine Chest Press"),
                Exercise("Incline Dumbbell Fly"),
                Exercise("Decline Dumbbell Press"),
                Exercise("Wide Push-up"),
                Exercise("Plyometric Push-up"),
                Exercise("Squeeze Press")
            ),
            "Buttock" to listOf(
                Exercise("Hip Thrust"),
                Exercise("Glute Kickback"),
                Exercise("Sumo Squat"),
                Exercise("Step-ups"),
                Exercise("Bulgarian Split Squat"),
                Exercise("Romanian Deadlift"),
                Exercise("Fire Hydrant"),
                Exercise("Glute Bridge"),
                Exercise("Single-Leg Glute Bridge"),
                Exercise("B-Stance Hip Thrust"),
                Exercise("Cable Pull-Through"),
                Exercise("Curtsy Lunge"),
                Exercise("Donkey Kicks"),
                Exercise("Frog Pumps"),
                Exercise("Lateral Band Walk"),
                Exercise("Hip Abduction (Band)")
            ),
            "Stomach" to listOf(
                Exercise("Crunch"),
                Exercise("Leg Raise"),
                Exercise("Plank"),
                Exercise("Russian Twist"),
                Exercise("Bicycle Crunch"),
                Exercise("Mountain Climbers"),
                Exercise("Flutter Kicks"),
                Exercise("Hanging Knee Raise"),
                Exercise("Side Plank"),
                Exercise("V-Up"),
                Exercise("Hollow Body Hold"),
                Exercise("Toe Touches"),
                Exercise("Dead Bug"),
                Exercise("Sit-Up"),
                Exercise("Reverse Crunch"),
                Exercise("Oblique Side Crunch")
            )
        )

        for ((category, exercises) in exercisesMap) {
            database.child(category).setValue(exercises)
        }
    }
}
