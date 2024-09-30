package com.example.merchantmart2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        firebaseAuth = FirebaseAuth.getInstance()

        Handler(mainLooper).postDelayed({
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
            }
            finish()
        }, 2000)
    }
}
