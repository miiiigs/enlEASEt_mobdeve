package com.mobdeve.mco.enleaset

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.mco.enleaset.ui.theme.EnlEASEtTheme

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var loginButton: Button
    private lateinit var idNumberEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        loginButton = findViewById(R.id.bt_login)
        idNumberEditText = findViewById(R.id.et_login_email)
        passwordEditText = findViewById(R.id.et_login_password)

        loginButton.setOnClickListener{
            val idNumber = idNumberEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (idNumber.isNotEmpty() && password.isNotEmpty()) {
                loginUser(idNumber, password)
            } else {
                Toast.makeText(this, "Please enter ID Number and Password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(id: String, password: String) {
        firestore.collection("students")
            .whereEqualTo("id", id.toInt())
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                } else {
                    val email = documents.documents[0].getString("email")
                    if (email != null) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    val errorMessage = task.exception?.message ?: "Unknown error"
                                    Log.e("LoginActivity", "signInWithEmailAndPassword failed: $errorMessage")
                                    Toast.makeText(this, "Login failed: $errorMessage", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Email not found for user", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
