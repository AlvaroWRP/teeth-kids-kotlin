package com.example.teethkids


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teethkids.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: EmergencyRequestsAdapter
    private val db: FirebaseFirestore = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Essa funcao eh chamada quando o Fragment eh criado e a UI esta sendo inicializada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchEmergencyRequests()
    }

    //Configura o RecyclerView
    private fun setupRecyclerView() {
        adapter = EmergencyRequestsAdapter { _, _ ->
            //Erro ao setar o RecyclerView
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    //Pega os chamados de emergencia do BD da colecao "emergency_requests"
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
                    val emergencyRequest = EmergencyRequest(id, title, description, city, imageUrl1, imageUrl2, imageUrl3, street, streetNumber)
                    emergencyRequests.add(emergencyRequest)
                }
                adapter.setData(emergencyRequests)
            }
            .addOnFailureListener { exception ->
                // Erro ao pegar os dados do BD
            }
    }
}
