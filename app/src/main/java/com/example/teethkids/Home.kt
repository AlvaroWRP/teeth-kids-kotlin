import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teethkids.EmergencyRequest
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchEmergencyRequests()
    }

    private fun setupRecyclerView() {
        adapter = EmergencyRequestsAdapter { emergencyRequest, _ ->
            // Handle accept or decline action
        }
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
                    val imageUrl = document.getString("imageUrl")
                    val title = document.getString("title")
                    val description = document.getString("description")
                    val emergencyRequest = EmergencyRequest(id, imageUrl, title, description)
                    emergencyRequests.add(emergencyRequest)
                }
                adapter.setData(emergencyRequests)
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }
}
