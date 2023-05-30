package com.example.teethkids

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.databinding.ItemEmergencyRequestBinding
import com.example.teethkids.EmergencyPopupDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide


class EmergencyRequestsAdapter(
    private val onActionClickListener: (emergencyRequest: EmergencyRequest, isAccepted: Boolean) -> Unit
) : RecyclerView.Adapter<EmergencyRequestsAdapter.ViewHolder>() {

    private val emergencyRequests: MutableList<EmergencyRequest> = mutableListOf()

    fun setData(data: List<EmergencyRequest>) {
        emergencyRequests.clear()
        emergencyRequests.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEmergencyRequestBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val emergencyRequest = emergencyRequests[position]
        holder.bind(emergencyRequest)
    }

    override fun getItemCount(): Int {
        return emergencyRequests.size
    }

    inner class ViewHolder(private val binding: ItemEmergencyRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(emergencyRequest: EmergencyRequest) {
            binding.titleTextView.text = emergencyRequest.title
            binding.descriptionTextView.text = emergencyRequest.description

            binding.acceptButton.setOnClickListener {
                getCurrentMedicId { medicId ->
                    if (medicId != null) {
                        val requestRef =
                            emergencyRequest.id?.let { it1 ->
                                FirebaseFirestore.getInstance().collection("emergency_requests").document(
                                    it1
                                )
                            }
                        requestRef?.get()?.addOnSuccessListener { documentSnapshot ->
                            val medics = documentSnapshot.get("medics") as? List<*>
                            if ((medics != null) && (medics.size < 5) && !medics.contains(medicId)) {
                                requestRef.update("medics", FieldValue.arrayUnion(medicId))
                                    .addOnSuccessListener {
                                        showPopup(emergencyRequest)
                                    }.addOnFailureListener {
                                        // Error storing medic ID
                                    }
                            } else {
                                // Maximum medics reached or medic already accepted the request
                            }
                        }?.addOnFailureListener {
                            // Error fetching request document
                        }
                    } else {
                        // Handle the case when the medic ID is not available
                    }
                } // Fetch the actual medic ID here
            }

            binding.declineButton.setOnClickListener {
                onActionClickListener(emergencyRequest, false)
            }
        }

        private fun getCurrentMedicId(callback: (String?) -> Unit) {
            val firebaseAuth = FirebaseAuth.getInstance()
            val currentUser = firebaseAuth.currentUser

            if (currentUser != null) {
                val currentUserId = currentUser.uid
                val usersCollection = FirebaseFirestore.getInstance().collection("users")
                val userDocument = usersCollection.document(currentUserId)

                userDocument.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            // Assuming the medic ID is stored as the document ID
                            callback.invoke(documentSnapshot.id)
                        } else {
                            // User document doesn't exist, handle accordingly
                            callback.invoke(null)
                        }
                    }
                    .addOnFailureListener {
                        // Error fetching user document, handle accordingly
                        callback.invoke(null)
                    }
            } else {
                callback.invoke(null)
            }
        }


        private fun showPopup(emergencyRequest: EmergencyRequest) {
            val dialog = Dialog(itemView.context)
            dialog.setContentView(R.layout.emergency_popup_dialog_fragment)

            // Initialize views
            val imageView1 = dialog.findViewById<ImageView>(R.id.imageView1)
            val imageView2 = dialog.findViewById<ImageView>(R.id.imageView2)
            val imageView3 = dialog.findViewById<ImageView>(R.id.imageView3)
            val descriptionTextView = dialog.findViewById<TextView>(R.id.descriptionTextView)
            val addressTextView = dialog.findViewById<TextView>(R.id.addressTextView)
            val closeButton = dialog.findViewById<Button>(R.id.closeButton)

            // Set data
            Glide.with(itemView.context).load(emergencyRequest.imageUrl1).into(imageView1)
            Glide.with(itemView.context).load(emergencyRequest.imageUrl2).into(imageView2)
            Glide.with(itemView.context).load(emergencyRequest.imageUrl3).into(imageView3)
            descriptionTextView.text = emergencyRequest.description
            val address = "${emergencyRequest.street}, ${emergencyRequest.streetNumber}, ${emergencyRequest.city}"
            addressTextView.text = address

            // Close button click listener
            closeButton.setOnClickListener {
                dialog.dismiss()
            }

            // Show the dialog
            dialog.show()
        }

    }
}

