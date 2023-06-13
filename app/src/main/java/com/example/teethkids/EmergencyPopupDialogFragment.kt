package com.example.teethkids

import android.content.Context
import android.content.Intent
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

        val emergencyRequest: EmergencyRequest? = arguments?.getParcelable("emergencyRequest")

        arguments?.let { args ->
            val imageUrl1 = args.getString(ARG_IMAGE_URL_1)
            val imageUrl2 = args.getString(ARG_IMAGE_URL_2)
            val imageUrl3 = args.getString(ARG_IMAGE_URL_3)
            val description = args.getString(ARG_DESCRIPTION)
            val street = args.getString(ARG_STREET)
            val streetNumber = args.getString(ARG_STREET_NUMBER)
            val city = args.getString(ARG_CITY)

            loadImage(imageUrl1, binding.imageView1)
            loadImage(imageUrl2, binding.imageView2)
            loadImage(imageUrl3, binding.imageView3)

            binding.descriptionTextView.text = description

            val address = buildAddressString(street, streetNumber, city)
            binding.addressTextView.text = address

            binding.endEmergencyButton.setOnClickListener {
                deleteEmergencyRequest(emergencyRequest)
            }

        }
    }

    private fun deleteEmergencyRequest(emergencyRequest: EmergencyRequest?) {
        if (emergencyRequest != null) {
            db.collection("emergency_requests")
                .document(emergencyRequest.id)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Emergency ended successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to end emergency", Toast.LENGTH_SHORT)
                        .show()
                }
        }
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

    companion object {
        private const val ARG_IMAGE_URL_1 = "image_url_1"
        private const val ARG_IMAGE_URL_2 = "image_url_2"
        private const val ARG_IMAGE_URL_3 = "image_url_3"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_STREET = "street"
        private const val ARG_STREET_NUMBER = "street_number"
        private const val ARG_CITY = "city"

        fun newInstance(emergencyRequest: EmergencyRequest): EmergencyPopupDialogFragment {
            val args = Bundle().apply {
                putString(ARG_IMAGE_URL_1, emergencyRequest.imageUrl1)
                putString(ARG_IMAGE_URL_2, emergencyRequest.imageUrl2)
                putString(ARG_IMAGE_URL_3, emergencyRequest.imageUrl3)
                putString(ARG_DESCRIPTION, emergencyRequest.description)
                putString(ARG_STREET, emergencyRequest.street)
                putString(ARG_STREET_NUMBER, emergencyRequest.streetNumber)
                putString(ARG_CITY, emergencyRequest.city)
            }
            val fragment = EmergencyPopupDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
