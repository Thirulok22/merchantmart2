package com.example.merchantmart2.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.merchantmart2.MyAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.example.merchantmart2.YourDataModel
import com.example.merchantmart2.databinding.FragmentSavedBinding
import android.widget.Toast
import com.example.merchantmart2.BookingActivity

class SavedFragment : Fragment() {

    private lateinit var binding: FragmentSavedBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var savedHousesRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.saveRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize MyAdapter with onSaveClick and onBookingClick
        adapter = MyAdapter(emptyList(),
            onSaveClick = { house -> saveHouseToFirebase(house.id) },
            onBookingClick = { house -> navigateToBooking(house) })

        recyclerView.adapter = adapter

        savedHousesRef = FirebaseDatabase.getInstance().reference.child("saved_houses")
        auth = Firebase.auth

        retrieveSavedHousesFromFirebase()
    }

    private fun retrieveSavedHousesFromFirebase() {
        val user = auth.currentUser
        user?.let {
            val userId = user.uid
            savedHousesRef.child(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val savedHouseIds = mutableListOf<String>()
                    for (data in snapshot.children) {
                        savedHouseIds.add(data.key ?: "")
                    }
                    retrieveDetailsOfSavedHouses(savedHouseIds)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled
                }
            })
        }
    }

    private fun retrieveDetailsOfSavedHouses(savedHouseIds: List<String>) {
        val housesRef = FirebaseDatabase.getInstance().reference.child("houses")
        housesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val savedHousesList = mutableListOf<YourDataModel>()
                for (houseId in savedHouseIds) {
                    val houseSnapshot = snapshot.child(houseId)
                    val ownerName = houseSnapshot.child("ownerName").value.toString()
                    val houseType = houseSnapshot.child("houseType").value.toString()
                    val address = houseSnapshot.child("address").value.toString()
                    val price = houseSnapshot.child("price").value.toString()
                    val contactDetails = houseSnapshot.child("contactDetails").value.toString()
                    val time = houseSnapshot.child("time").value.toString()
                    val rentInclusions = houseSnapshot.child("rentInclusions").value.toString()
                    val facilities = houseSnapshot.child("facilities").value.toString()
                    val cityName = houseSnapshot.child("cityName").value.toString()

                    val imageUrls = mutableListOf<String>()
                    for (imageData in houseSnapshot.child("imageUrls").children) {
                        val imageUrl = imageData.value.toString()
                        imageUrls.add(imageUrl)
                    }

                    val id = houseSnapshot.key ?: ""

                    val houseModel = YourDataModel(
                        id,
                        houseId,
                        ownerName,
                        houseType,
                        address,
                        price,
                        "", // No advance amount needed in this fragment
                        contactDetails,
                        time,
                        rentInclusions,
                        facilities,
                        cityName,
                        imageUrls
                    )
                    savedHousesList.add(houseModel)
                }
                adapter.houseList = savedHousesList
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }

    private fun saveHouseToFirebase(houseId: String) {
        val savedHousesRef = FirebaseDatabase.getInstance().reference.child("saved_houses")
        val user = Firebase.auth.currentUser
        user?.let {
            val userId = user.uid
            savedHousesRef.child(userId).child(houseId).setValue(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // House saved successfully
                        Toast.makeText(
                            requireContext(),
                            "House saved successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Error saving house
                        Toast.makeText(
                            requireContext(),
                            "Failed to save house",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun navigateToBooking(house: YourDataModel) {
        val intent = Intent(requireContext(), BookingActivity::class.java)
        intent.putExtra("house", house) // Pass the selected house to BookingActivity
        startActivity(intent)
    }
}
