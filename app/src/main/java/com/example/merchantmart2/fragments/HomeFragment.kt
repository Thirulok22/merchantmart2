package com.example.merchantmart2.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.merchantmart2.YourDataModel
import com.example.merchantmart2.MyAdapter
import com.example.merchantmart2.BookingActivity
import com.example.merchantmart2.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.io.Serializable

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyAdapter(
            emptyList(),
            onSaveClick = { house -> saveHouseToFirebase(house.id) },
            onBookingClick = { house -> navigateToBooking(house) }
        )
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().reference.child("houses")
        auth = Firebase.auth

        // Retrieve data from Firebase and update RecyclerView initially
        retrieveDataFromFirebase()

        // Set click listener for the search button
        binding.btnSearch.setOnClickListener {
            val searchText = binding.etSearch.text.toString().trim()
            if (searchText.isNotEmpty()) {
                searchHouses(searchText)
            } else {
                // If search text is empty, show all houses
                retrieveDataFromFirebase()
            }
        }
    }

    private fun retrieveDataFromFirebase() {
        val housesList = mutableListOf<YourDataModel>()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val id = data.key ?: ""
                    val houseId =data.key ?:""
                    val ownerName = data.child("ownerName").value.toString()
                    val houseType = data.child("houseType").value.toString()
                    val address = data.child("address").value.toString()
                    val price = data.child("price").value.toString()
                    val advanceAmount = data.child("advanceAmount").value.toString()
                    val contactDetails = data.child("contactDetails").value.toString()
                    val time = data.child("time").value.toString()
                    val rentInclusions = data.child("rentInclusions").value.toString()
                    val facilities = data.child("facilities").value.toString()
                    val cityName = data.child("cityName").value.toString()

                    val imageUrls = mutableListOf<String>()
                    for (imageData in data.child("imageUrls").children) {
                        val imageUrl = imageData.value.toString()
                        imageUrls.add(imageUrl)
                    }

                    val houseModel = YourDataModel(
                        id,
                        houseId,
                        ownerName,
                        houseType,
                        address,
                        price,
                        advanceAmount,
                        contactDetails,
                        time,
                        rentInclusions,
                        facilities,
                        cityName,
                        imageUrls
                    )
                    housesList.add(houseModel)
                }
                // Update the RecyclerView adapter with houses
                adapter.houseList = housesList
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }

    private fun searchHouses(searchText: String) {
        val housesList = mutableListOf<YourDataModel>()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val cityName = data.child("cityName").value.toString()
                    // Filter houses based on the searched city or locality
                    if (cityName.contains(searchText, ignoreCase = true)) {
                        val id = data.key ?: ""
                        val houseId = data.key ?: ""
                        val ownerName = data.child("ownerName").value.toString()
                        val houseType = data.child("houseType").value.toString()
                        val address = data.child("address").value.toString()
                        val price = data.child("price").value.toString()
                        val advanceAmount = data.child("advanceAmount").value.toString()
                        val contactDetails = data.child("contactDetails").value.toString()
                        val time = data.child("time").value.toString()
                        val rentInclusions = data.child("rentInclusions").value.toString()
                        val facilities = data.child("facilities").value.toString()

                        val imageUrls = mutableListOf<String>()
                        for (imageData in data.child("imageUrls").children) {
                            val imageUrl = imageData.value.toString()
                            imageUrls.add(imageUrl)
                        }

                        val houseModel = YourDataModel(
                            id,
                            houseId,
                            ownerName,
                            houseType,
                            address,
                            price,
                            advanceAmount,
                            contactDetails,
                            time,
                            rentInclusions,
                            facilities,
                            cityName,
                            imageUrls
                        )
                        housesList.add(houseModel)
                    }
                }
                // Update the RecyclerView adapter with filtered houses
                adapter.houseList = housesList
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
                        Toast.makeText(requireContext(), "Failed to save house", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun navigateToBooking(house: YourDataModel) {
        val intent = Intent(requireContext(), BookingActivity::class.java)
        intent.putExtra("house", house as Serializable) // Pass the selected house to BookingActivity
        startActivity(intent)
    }

}
