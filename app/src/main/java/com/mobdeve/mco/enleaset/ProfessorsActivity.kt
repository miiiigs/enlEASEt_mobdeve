package com.mobdeve.mco.enleaset

import android.content.Intent
import android.os.Bundle
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
    private var professorList: MutableList<Professor> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_professors)

        homeIconImageView = findViewById(R.id.iv_home_icon)
        homeIconImageView.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        firestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.rv_professors)
        recyclerView.layoutManager = LinearLayoutManager(this)
        professorAdapter = ProfessorAdapter(professorList)
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
                professorAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}