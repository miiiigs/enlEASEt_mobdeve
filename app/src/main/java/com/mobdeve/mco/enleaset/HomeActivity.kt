package com.mobdeve.mco.enleaset

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {

    private lateinit var professorButton: Button
    private lateinit var scheduleButton: Button
    private lateinit var coursesButton: Button
    private lateinit var flowchartButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        flowchartButton = findViewById(R.id.bt_home_flowchart)
        flowchartButton.setOnClickListener {
            val intent = Intent(this, FlowchartActivity::class.java)
            startActivity(intent)
        }

        coursesButton = findViewById(R.id.bt_home_courses)
        coursesButton.setOnClickListener {
            val intent = Intent(this, CoursesActivity::class.java)
            startActivity(intent)
        }

        scheduleButton = findViewById(R.id.bt_home_schedule)
        scheduleButton.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

        professorButton = findViewById(R.id.bt_home_professors)
        professorButton.setOnClickListener {
            val intent = Intent(this, ProfessorsActivity::class.java)
            startActivity(intent)
        }
    }
}