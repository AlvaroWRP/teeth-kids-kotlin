package com.example.teethkids


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.teethkids.databinding.EmergencyPopupDialogFragmentBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions


class EmergencyPopupDialogFragment : DialogFragment() {

    private var _binding: EmergencyPopupDialogFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = EmergencyPopupDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the passed data
        val imageUrl1 = arguments?.getString("imageUrl1")
        val imageUrl2 = arguments?.getString("imageUrl2")
        val imageUrl3 = arguments?.getString("imageUrl3")
        val description = arguments?.getString("description")
        val street = arguments?.getString("street")
        val streetNumber = arguments?.getString("streetNumber")
        val city = arguments?.getString("city")

        // Display the data in the dialog views
        loadImage(imageUrl1, binding.imageView1)
        loadImage(imageUrl2, binding.imageView2)
        loadImage(imageUrl3, binding.imageView3)
        binding.descriptionTextView.text = description
        val formattedAddress = "$street $streetNumber, $city"
        binding.addressTextView.text = formattedAddress

        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.emergency_popup_dialog_fragment, null)

        // Retrieve the passed data
        val imageUrl1 = arguments?.getString("imageUrl1")
        val imageUrl2 = arguments?.getString("imageUrl2")
        val imageUrl3 = arguments?.getString("imageUrl3")
        val description = arguments?.getString("description")
        val street = arguments?.getString("street")
        val streetNumber = arguments?.getString("streetNumber")
        val city = arguments?.getString("city")

        // Display the data in the dialog views
        val imageView1 = dialogView.findViewById<ImageView>(R.id.imageView1)
        val imageView2 = dialogView.findViewById<ImageView>(R.id.imageView2)
        val imageView3 = dialogView.findViewById<ImageView>(R.id.imageView3)
        val descriptionTextView = dialogView.findViewById<TextView>(R.id.descriptionTextView)
        val addressTextView = dialogView.findViewById<TextView>(R.id.addressTextView)

        // Load the images using an image loading library like Glide or Picasso
        // Example with Glide:
        Glide.with(requireContext()).load(imageUrl1).into(imageView1)
        Glide.with(requireContext()).load(imageUrl2).into(imageView2)
        Glide.with(requireContext()).load(imageUrl3).into(imageView3)

        descriptionTextView.text = description

        // Format and display the address
        val formattedAddress = "$street $streetNumber, $city"
        addressTextView.text = formattedAddress

        return builder.create()
    }


    private fun loadImage(imageUrl: String?, imageView: ImageView) {
        Glide.with(requireContext())
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.placeholder_image) // Placeholder image resource
                    .error(R.drawable.error_image) // Error image resource
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): EmergencyPopupDialogFragment {
            return EmergencyPopupDialogFragment()
        }
    }
}

