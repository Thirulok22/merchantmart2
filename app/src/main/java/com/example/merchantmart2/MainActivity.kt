package com.example.merchantmart2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.merchantmart2.databinding.ActivityMainBinding
import com.example.merchantmart2.fragments.HomeFragment
import com.example.merchantmart2.fragments.ProfileFragment
import com.example.merchantmart2.fragments.SavedFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // Declare firebaseAuth here
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val savedFragment = SavedFragment() // Corrected variable name

        makeCurrentFragment(homeFragment)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> makeCurrentFragment(homeFragment)
                R.id.Profile -> makeCurrentFragment(profileFragment)
                R.id.savedhouse -> makeCurrentFragment(savedFragment) // Corrected variable name
            }
            true
        }

        // Initialize firebaseAuth here
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
}
