import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.EmergencyRequest
import com.example.teethkids.R
import com.example.teethkids.databinding.ItemEmergencyRequestBinding

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
            // Set the emergency request data to the views using the binding object
            binding.requestImageView.setImageResource(R.drawable.baseline_account_circle_24) // Set your image here
            binding.acceptButton.setOnClickListener {
                onActionClickListener(emergencyRequest, true)
            }
            binding.declineButton.setOnClickListener {
                onActionClickListener(emergencyRequest, false)
            }
        }
    }
}