package com.mobdeve.mco.enleaset

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.mco.enleaset.adapter.ProfessorAdapter
import com.mobdeve.mco.enleaset.model.Professor

class ProfessorsActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var professorAdapter: ProfessorAdapter
    private lateinit var homeIconImageView: ImageView
    private lateinit var searchEditText: EditText
    private var professorList: MutableList<Professor> = mutableListOf()
    private var filteredList: MutableList<Professor> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_professors)

        searchEditText = findViewById(R.id.et_search)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                filteredList.clear()
                if (query.isEmpty()) {
                    filteredList.addAll(professorList)
                } else {
                    for (professor in professorList) {
                        if (professor.firstname.contains(query, true) ||
                            professor.lastname.contains(query, true) ||
                            professor.department.contains(query, true) ||
                            professor.course.any { it.contains(query, true) }
                        ) {
                            filteredList.add(professor)
                        }
                    }
                }
                professorAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        homeIconImageView = findViewById(R.id.iv_home_icon)
        homeIconImageView.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        firestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.rv_professors)
        recyclerView.layoutManager = LinearLayoutManager(this)
        professorAdapter = ProfessorAdapter(filteredList)
        recyclerView.adapter = professorAdapter

        fetchProfessors()

    }

    private fun fetchProfessors() {
        firestore.collection("professors")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val professor = document.toObject(Professor::class.java)
                    professorList.add(professor)
                }
                filteredList.addAll(professorList)
                professorAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}