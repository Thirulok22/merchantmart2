package com.example.merchantmart2.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.merchantmart2.R
import com.example.merchantmart2.SignInActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signOutBtn: Button
    private lateinit var tvProfileNickname: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var matETProfileNickname: TextInputEditText
    private lateinit var btnUpdateProfile: MaterialButton
    private var nickname = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        tvProfileNickname = view.findViewById(R.id.tvProfileNickname)
        matETProfileNickname = view.findViewById(R.id.matETProfileNickname)
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile)

        // Handle Sign Out
        signOutBtn = view.findViewById(R.id.logout_button)
        signOutBtn.setOnClickListener { handleSignOut() }

        tvProfileEmail = view.findViewById(R.id.tvProfileEmail)
        tvProfileEmail.text = auth.currentUser?.email

        nickname = auth.currentUser?.displayName ?: "Adventurer"
        tvProfileNickname.text = nickname
        matETProfileNickname.setText(nickname)

        // Enable update profile button when the user edits text in the profile section
        matETProfileNickname.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for this case
            }

            override fun afterTextChanged(editable: Editable?) {
                // Enable the button if the nickname has changed
                btnUpdateProfile.isEnabled = editable.toString() != nickname
            }
        })



        // Add handler for profile update
        btnUpdateProfile.setOnClickListener { handleProfileUpdate() }

        return view
    }

    private fun handleProfileUpdate() {
        val newNickname = matETProfileNickname.text.toString()
        if (newNickname.isNotEmpty() && newNickname != nickname) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(newNickname)
                .build()

            auth.currentUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Nickname Changed", Toast.LENGTH_LONG).show()
                        nickname = newNickname
                        tvProfileNickname.text = newNickname
                    } else {
                        Toast.makeText(requireContext(), "Failed to update nickname", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun handleSignOut() {
        auth.signOut()
        val intent = Intent(requireContext(), SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}

