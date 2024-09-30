package com.example.merchantmart2

import java.io.Serializable

data class BookingDetails(
    val houseId: String?,
    val selectedDateTime: String
)

data class YourDataModel(
    val id: String,
    val houseId: String,
    val ownerName: String,
    val houseType: String,
    val address: String,
    val price: String,
    val advanceAmount: String,
    val contactDetails: String,
    val time: String,
    val rentInclusions: String,
    val facilities: String,
    val cityName: String,
    val imageUrls: List<String>
) : Serializable

