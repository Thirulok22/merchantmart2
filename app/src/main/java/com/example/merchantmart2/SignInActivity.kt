package com.example.merchantmart2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.merchantmart2.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Set onClickListener for "Not Registered Yet, Sign Up!" TextView
        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Set onClickListener for "Login as Owner" Button
        binding.ownerbutton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Start OwnerActivity with the same email and password
                        val intent = Intent(this, OwnerMainActivity::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("password", pass)
                        startActivity(intent)
                        // Display welcome message
                        Toast.makeText(this, "Login successful. Welcome to Rent Easy! Find your dream home here.", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorMessage = task.exception?.message ?: "Unknown error occurred"
                        if (errorMessage.contains("password")) {
                            Toast.makeText(this, "Incorrect password. Please enter correct password.", Toast.LENGTH_SHORT).show()
                        } else if (errorMessage.contains("email address")) {
                            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed.", Toast.LENGTH_SHORT).show()
            }
        }

        // Set onClickListener for "Login" Button
        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        // Display welcome message
                        Toast.makeText(this, "Login successful. Welcome to Rent Easy! Find your dream home here.", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorMessage = task.exception?.message ?: "Unknown error occurred"
                        if (errorMessage.contains("password")) {
                            Toast.makeText(this, "Incorrect password. Please enter correct password.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
