package com.example.merchantmart2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(
    var houseList: List<YourDataModel>,
    private val onSaveClick: (YourDataModel) -> Unit,
    private val onBookingClick: (YourDataModel) -> Unit // Add booking click listener
) : RecyclerView.Adapter<MyAdapter.HouseViewHolder>() {

    inner class HouseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ownerNameTextView: TextView = itemView.findViewById(R.id.ownerNameTextView)
        val cityTextView: TextView = itemView.findViewById(R.id.cityTextView)
        val houseTypeTextView: TextView = itemView.findViewById(R.id.houseTypeTextView)
        val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val advanceAmountTextView: TextView = itemView.findViewById(R.id.advanceTextView)
        val contactDetailsTextView: TextView = itemView.findViewById(R.id.contactDetailsTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val rentInclusionsTextView: TextView = itemView.findViewById(R.id.rentInclusionsTextView)
        val facilitiesTextView: TextView = itemView.findViewById(R.id.facilitiesTextView)
        val imageView0: ImageView = itemView.findViewById(R.id.ImageView0)
        val imageView1: ImageView = itemView.findViewById(R.id.ImageView1)
        val imageView2: ImageView = itemView.findViewById(R.id.ImageView2)
        private val saveButton: ImageButton = itemView.findViewById(R.id.SaveButton)
        private val bookingButton: ImageButton = itemView.findViewById(R.id.booking) // Booking button

        init {
            // Set click listeners
            saveButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = houseList[position]
                    onSaveClick(currentItem)
                }
            }

            bookingButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = houseList[position]
                    onBookingClick(currentItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_house, parent, false)
        return HouseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        val currentItem = houseList[position]

        holder.ownerNameTextView.text = currentItem.ownerName
        holder.cityTextView.text = currentItem.cityName
        holder.houseTypeTextView.text = currentItem.houseType
        holder.addressTextView.text = currentItem.address
        holder.priceTextView.text = currentItem.price
        holder.advanceAmountTextView.text = currentItem.advanceAmount
        holder.contactDetailsTextView.text = currentItem.contactDetails
        holder.timeTextView.text = currentItem.time
        holder.rentInclusionsTextView.text = currentItem.rentInclusions
        holder.facilitiesTextView.text = currentItem.facilities

        // Load images using Glide
        // Assuming that imageUrls list contains exactly 3 image URLs
        Glide.with(holder.imageView0)
            .load(currentItem.imageUrls.getOrElse(0) { "" }) // Load the first image URL
            .placeholder(R.drawable.placeholder_image) // Placeholder image
            .error(R.drawable.error_image) // Error image
            .centerCrop()
            .into(holder.imageView0)

        Glide.with(holder.imageView1)
            .load(currentItem.imageUrls.getOrElse(1) { "" }) // Load the second image URL
            .placeholder(R.drawable.placeholder_image) // Placeholder image
            .error(R.drawable.error_image) // Error image
            .centerCrop()
            .into(holder.imageView1)

        Glide.with(holder.imageView2)
            .load(currentItem.imageUrls.getOrElse(2) { "" }) // Load the third image URL
            .placeholder(R.drawable.placeholder_image) // Placeholder image
            .error(R.drawable.error_image) // Error image
            .centerCrop()
            .into(holder.imageView2)
    }

    override fun getItemCount() = houseList.size
}
