package com.example.teethkids

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.teethkids.databinding.EmergencyPopupDialogFragmentBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class EmergencyPopupDialogFragment : DialogFragment() {
    private lateinit var binding: EmergencyPopupDialogFragmentBinding
    private val db: FirebaseFirestore = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EmergencyPopupDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = EmergencyPopupDialogFragmentBinding.bind(view)

        val emergencyRequest: EmergencyRequest? = arguments?.getParcelable("emergencyRequest")

        emergencyRequest?.let { request ->
            loadImage(request.imageUrl1, binding.imageView1)
            loadImage(request.imageUrl2, binding.imageView2)
            loadImage(request.imageUrl3, binding.imageView3)

            binding.descriptionTextView.text = request.description

            val address = buildAddressString(request.street, request.streetNumber, request.city)
            binding.addressTextView.text = address

            binding.openMapsButton.setOnClickListener {
                openGoogleMaps(request.street, request.streetNumber, request.city)
            }

            binding.endEmergencyButton.setOnClickListener {
                deleteEmergencyRequest(request)
            }
        }
    }

    private fun deleteEmergencyRequest(emergencyRequest: EmergencyRequest?) {
        if (emergencyRequest != null) {
            db.collection("emergency_requests")
                .document(emergencyRequest.id)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Emergency ended successfully", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to end emergency", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun openGoogleMaps(street: String?, streetNumber: String?, city: String?) {
        val address = Uri.encode("$street $streetNumber, $city")
        val gmmIntentUri = Uri.parse("geo:0,0?q=$address")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun loadImage(imageUrl: String?, imageView: ImageView) {
        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(imageView)
        }
    }

    private fun buildAddressString(street: String?, streetNumber: String?, city: String?): String {
        val addressBuilder = StringBuilder()
        if (!street.isNullOrEmpty()) {
            addressBuilder.append(street)
        }
        if (!streetNumber.isNullOrEmpty()) {
            addressBuilder.append(", $streetNumber")
        }
        if (!city.isNullOrEmpty()) {
            addressBuilder.append(", $city")
        }
        return addressBuilder.toString()
    }
}



