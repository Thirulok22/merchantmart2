package com.example.merchantmart2

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailActivity : AppCompatActivity() {
    private lateinit var ownerNameEditText: EditText
    private lateinit var houseTypeEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var contactDetailsEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var rentInclusionEditText: EditText
    private lateinit var facilitiesEditText: EditText
    private lateinit var cityNameEditText: EditText
    private lateinit var advanceAmountEditText: EditText
    private lateinit var updateDetailsButton: Button
    private lateinit var uploadImageButton: Button
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val storageReference: StorageReference = firebaseStorage.reference

    private var imageUris = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        auth = Firebase.auth

        ownerNameEditText = findViewById(R.id.ownername)
        houseTypeEditText = findViewById(R.id.housetype)
        addressEditText = findViewById(R.id.address_edittext)
        priceEditText = findViewById(R.id.price_edittext)
        contactDetailsEditText = findViewById(R.id.contact_details_edittext)
        timeEditText = findViewById(R.id.time)
        rentInclusionEditText = findViewById(R.id.rentinclusion)
        facilitiesEditText = findViewById(R.id.facilities)
        cityNameEditText = findViewById(R.id.cityname)
        advanceAmountEditText = findViewById(R.id.advance_edittext)
        updateDetailsButton = findViewById(R.id.update_details_button)
        uploadImageButton = findViewById(R.id.upload_images_button)
        imageView1 = findViewById(R.id.house_image_1)
        imageView2 = findViewById(R.id.house_image_2)
        imageView3 = findViewById(R.id.house_image_3)

        databaseReference = FirebaseDatabase.getInstance().reference.child("houses")

        uploadImageButton.setOnClickListener { chooseImages() }
        updateDetailsButton.setOnClickListener { saveHouseDetails() }
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            if (uris.isNotEmpty()) {
                imageUris.clear()
                imageUris.addAll(uris)
                setImages()
            }
        }

    private fun chooseImages() {
        getContent.launch("image/*")
    }

    private fun setImages() {
        if (imageUris.size >= 1) {
            imageView1.setImageURI(imageUris[0])
        }
        if (imageUris.size >= 2) {
            imageView2.setImageURI(imageUris[1])
        }
        if (imageUris.size >= 3) {
            imageView3.setImageURI(imageUris[2])
        }
    }

    private fun saveHouseDetails() {
        val ownerName = ownerNameEditText.text.toString().trim()
        val houseType = houseTypeEditText.text.toString().trim()
        val address = addressEditText.text.toString().trim()
        val price = priceEditText.text.toString().trim()
        val contactDetails = contactDetailsEditText.text.toString().trim()
        val time = timeEditText.text.toString().trim()
        val rentInclusions = rentInclusionEditText.text.toString().trim()
        val facilities = facilitiesEditText.text.toString().trim()
        val cityName = cityNameEditText.text.toString().trim()
        val advanceAmount = advanceAmountEditText.text.toString().trim()

        if (ownerName.isEmpty() || houseType.isEmpty() || address.isEmpty() || price.isEmpty() || contactDetails.isEmpty() || time.isEmpty() || rentInclusions.isEmpty() || facilities.isEmpty() || cityName.isEmpty() || advanceAmount.isEmpty()) {
            Toast.makeText(this, "Please fill in all the details", Toast.LENGTH_SHORT).show()
            return
        }

        val houseId = databaseReference.push().key // Generate unique ID
        val houseRef = databaseReference.child(houseId!!) // Use the generated ID
        houseRef.child("ownerName").setValue(ownerName)
        houseRef.child("houseType").setValue(houseType)
        houseRef.child("address").setValue(address)
        houseRef.child("price").setValue(price)
        houseRef.child("contactDetails").setValue(contactDetails)
        houseRef.child("time").setValue(time)
        houseRef.child("rentInclusions").setValue(rentInclusions)
        houseRef.child("facilities").setValue(facilities)
        houseRef.child("cityName").setValue(cityName)
        houseRef.child("advanceAmount").setValue(advanceAmount) // Save advance amount

        // Upload images
        for ((index, uri) in imageUris.withIndex()) {
            val imageRef = storageReference.child("images/${houseId}_$index.jpg")
            imageRef.putFile(uri)
                .addOnSuccessListener { _ ->
                    imageRef.downloadUrl.addOnSuccessListener { imageUri ->
                        houseRef.child("imageUrls").child("image$index").setValue(imageUri.toString())

                        // If all images are uploaded, show success message
                        if (index == imageUris.size - 1) {
                            Toast.makeText(this, "House details saved successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
