package com.example.merchantmart2
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.merchantmart2.databinding.ActivityOwnerMainBinding
import com.google.firebase.auth.FirebaseAuth

class OwnerMainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityOwnerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Handle logout button click
        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            // Redirect to login or another appropriate screen
            // For simplicity, let's finish the activity for now
            finish()
        }

        // Handle ImageButton click to navigate to ManagedetailActivity
        binding.squareImageButton.setOnClickListener {
            // Start Managedetail activity
            startActivity(Intent(this,DetailActivity::class.java))
        }
    }
}
