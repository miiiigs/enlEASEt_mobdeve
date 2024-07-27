package com.mobdeve.mco.enleaset

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.mobdeve.mco.enleaset.adapter.CourseOfferingAdapter
import com.mobdeve.mco.enleaset.model.Course
import com.mobdeve.mco.enleaset.model.CourseOffering
import com.mobdeve.mco.enleaset.model.Professor

class CoursesActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var courseOfferingAdapter: CourseOfferingAdapter
    private var courseOfferingList: MutableList<CourseOffering> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_courses)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.rv_courses)
        recyclerView.layoutManager = LinearLayoutManager(this)
        courseOfferingAdapter = CourseOfferingAdapter(courseOfferingList)
        recyclerView.adapter = courseOfferingAdapter

        fetchCourseOfferings()

//        // Check if user is authenticated
//        if (auth.currentUser != null) {
//            // Fetch data from Firestore
//            fetchCourseOfferings()
//        } else {
//            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
//            // Redirect to login screen or handle unauthenticated state
//        }
    }

    private fun fetchCourseOfferings() {


        firestore.collection("courses_offered")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val courseOffering = document.toObject(CourseOffering::class.java)
                    Log.d("CourseOffering", courseOffering.toString())
                    courseOffering.courseRef?.get()?.addOnSuccessListener { courseDoc ->
                        val course = courseDoc.toObject<Course>()

                        courseOffering.professorRef?.get()?.addOnSuccessListener { professorDoc ->
                            val professor = professorDoc.toObject<Professor>()

                            courseOfferingList.add(
                                CourseOffering(
                                    courseRef = courseOffering.courseRef,
                                    professorRef = courseOffering.professorRef,
                                    slots = courseOffering.slots,
                                    slotsTaken = courseOffering.slotsTaken,
                                    availability = courseOffering.availability,
                                    course = course,
                                    professor = professor
                                )
                            )
                            courseOfferingAdapter.notifyDataSetChanged()
                        }?.addOnFailureListener { exception ->
                            Toast.makeText(this, "Error fetching professor: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    }?.addOnFailureListener { exception ->
                        Toast.makeText(this, "Error fetching course: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}