package com.example.merchantmart2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class BookingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var selectedDateTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        auth = Firebase.auth

        // Retrieve house data from intent
        val house = intent.getSerializableExtra("house") as? YourDataModel
        val houseId = intent.getStringExtra("houseId")

        house?.let {
            // Update UI with house details
            findViewById<TextView>(R.id.ownerNameTextView).text = it.ownerName
            findViewById<TextView>(R.id.cityTextView).text = it.cityName
            findViewById<TextView>(R.id.houseTypeTextView).text = it.houseType
            findViewById<TextView>(R.id.addressTextView).text = it.address
            findViewById<TextView>(R.id.priceTextView).text = it.price
            findViewById<TextView>(R.id.advanceTextView).text = it.advanceAmount
            findViewById<TextView>(R.id.contactDetailsTextView).text = it.contactDetails
            findViewById<TextView>(R.id.rentInclusionsTextView).text = it.rentInclusions
            findViewById<TextView>(R.id.facilitiesTextView).text = it.facilities
        }

        // Choose Date & Time Button Click Listener
        findViewById<Button>(R.id.chooseDateTimeButton).setOnClickListener {
            showDateTimePicker()
        }

        // Confirm Booking Button Click Listener
        findViewById<Button>(R.id.confirmBookingButton).setOnClickListener {
            if (selectedDateTime.isNotEmpty()) {
                saveBookingDetailsToDatabase(houseId, selectedDateTime)
                Toast.makeText(this, "Slot booked successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please select a date and time!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timePicker = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)

                        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        selectedDateTime = dateFormat.format(calendar.time)
                        findViewById<TextView>(R.id.selectedDateTimeTextView).text = selectedDateTime
                        findViewById<TextView>(R.id.selectedDateTimeTextView).visibility = View.VISIBLE
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                )
                timePicker.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }

    private fun saveBookingDetailsToDatabase(houseId: String?, selectedDateTime: String) {
        val userId = auth.currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/bookings/$userId").push()
        val bookingId = ref.key
        val bookingDetails = BookingDetails(houseId, selectedDateTime)

        if (bookingId != null) {
            ref.setValue(bookingDetails)
                .addOnSuccessListener {
                    // Handle success
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
    }
}
