package com.mobdeve.mco.enleaset

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
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
    private lateinit var homeIconImageView: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var courseOfferingAdapter: CourseOfferingAdapter
    private var courseOfferingList: MutableList<CourseOffering> = mutableListOf()
    private var filteredList: MutableList<CourseOffering> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_courses)

        searchEditText = findViewById(R.id.et_search)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                filteredList.clear()
                if (query.isEmpty()) {
                    filteredList.addAll(courseOfferingList)
                } else {
                    for (courseOffering in courseOfferingList) {
                        if (courseOffering.course?.name?.lowercase()?.contains(query) == true ||
                            courseOffering.course?.code?.lowercase()?.contains(query) == true ||
                            courseOffering.location.lowercase().contains(query) ||
                            courseOffering.professor?.firstname?.lowercase()?.contains(query) == true ||
                            courseOffering.professor?.lastname?.lowercase()?.contains(query) == true) {
                            filteredList.add(courseOffering)
                        }
                    }
                }
                courseOfferingAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        homeIconImageView = findViewById(R.id.iv_home_icon)
        homeIconImageView.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.rv_courses)
        recyclerView.layoutManager = LinearLayoutManager(this)
        courseOfferingAdapter = CourseOfferingAdapter(filteredList)
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
                    courseOffering.courseRef = document.getDocumentReference("courseRef")
                    courseOffering.professorRef = document.getDocumentReference("professorRef")
                    courseOffering.courseRef?.get()?.addOnSuccessListener { courseDoc ->
                        val course = courseDoc.toObject(Course::class.java)
                        courseOffering.course = course

                        courseOffering.professorRef?.get()?.addOnSuccessListener { professorDoc ->
                            val professor = professorDoc.toObject(Professor::class.java)
                            courseOffering.professor = professor

                            courseOfferingList.add(courseOffering)
                            filteredList.add(courseOffering)
                            Log.d("CourseOffering", courseOffering.toString())


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