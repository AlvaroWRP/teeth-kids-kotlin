package com.example.teethkids

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teethkids.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: EmergencyRequestsAdapter
    private val db: FirebaseFirestore = Firebase.firestore
    private lateinit var currentMedicId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchEmergencyRequests()
        fetchCurrentMedicId()
    }

    private fun setupRecyclerView() {
        adapter = EmergencyRequestsAdapter(
            onItemClick = { emergencyRequest -> showPopupDialog(emergencyRequest) }
        ) { emergencyRequest -> declineEmergencyRequest(emergencyRequest) }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun fetchEmergencyRequests() {
        db.collection("emergency_requests")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val emergencyRequests = mutableListOf<EmergencyRequest>()
                for (document in querySnapshot) {
                    val id = document.id
                    val title = document.getString("title")
                    val description = document.getString("description")
                    val city = document.getString("city")
                    val imageUrl1 = document.getString("imageUrl1")
                    val imageUrl2 = document.getString("imageUrl2")
                    val imageUrl3 = document.getString("imageUrl3")
                    val street = document.getString("street")
                    val streetNumber = document.getString("streetNumber")
                    val emergencyRequest = EmergencyRequest(
                        id, title, description, city,
                        imageUrl1, imageUrl2, imageUrl3, street, streetNumber
                    )
                    emergencyRequests.add(emergencyRequest)
                }
                adapter.setData(emergencyRequests)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Erro ao pegar dados do Banco de Dados", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchCurrentMedicId() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        currentMedicId = document.id
                        val isActive = document.getBoolean("isActive") ?: false
                        if (!isActive) {
                            binding.recyclerView.visibility = View.GONE
                            binding.messageTextView.visibility = View.VISIBLE
                        } else {
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.messageTextView.visibility = View.GONE
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Erro ao obter o ID do médico", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showPopupDialog(emergencyRequest: EmergencyRequest) {
        val popupDialogFragment = EmergencyPopupDialogFragment()
        val bundle = Bundle()
        bundle.putParcelable("emergencyRequest", emergencyRequest)
        popupDialogFragment.arguments = bundle
        popupDialogFragment.show(parentFragmentManager, "emergency_popup")

    }

    private fun declineEmergencyRequest(emergencyRequest: EmergencyRequest) {
        db.collection("emergency_requests")
            .document(emergencyRequest.id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val medics = documentSnapshot.get("medics") as? List<*>
                if (medics != null && medics.contains(currentMedicId)) {
                    val updatedMedics = medics.filter { it != currentMedicId }
                    db.collection("emergency_requests")
                        .document(emergencyRequest.id)
                        .update("medics", updatedMedics)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Chamado recusado com sucesso", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Erro ao recusar o chamado", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Chamado já recusado anteriormente", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Erro ao recusar o chamado", Toast.LENGTH_SHORT).show()
            }
    }
}
