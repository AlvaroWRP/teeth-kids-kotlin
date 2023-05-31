package com.example.teethkids

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.databinding.ItemEmergencyRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.bumptech.glide.Glide


class EmergencyRequestsAdapter(
    private val onItemClick: (EmergencyRequest) -> Unit,
    private val onDeclineClick: (EmergencyRequest) -> Unit
) : RecyclerView.Adapter<EmergencyRequestsAdapter.ViewHolder>() {

    private val emergencyRequests: MutableList<EmergencyRequest> = mutableListOf()

    //Responsavel por atualizar o data set do adapter com uma nova lista de objetos do "EmergencyRequest"
    fun setData(data: List<EmergencyRequest>) {
        emergencyRequests.clear()
        emergencyRequests.addAll(data)
        notifyDataSetChanged()
    }

    //Eh chamada pelo RecyclerView pra criar um novo ViewHolder para os itens da lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEmergencyRequestBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    //Esse metodo basicamente atualiza os conteudos do RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val emergencyRequest = emergencyRequests[position]
        holder.bind(emergencyRequest)
    }

    //Retorna o numero total de itens dentro do data set
    override fun getItemCount(): Int {
        return emergencyRequests.size
    }

    inner class ViewHolder(private val binding: ItemEmergencyRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //Funcao de viewBinding
        fun bind(emergencyRequest: EmergencyRequest) {
            binding.titleTextView.text = emergencyRequest.title
            binding.descriptionTextView.text = emergencyRequest.description

            val context = itemView.context

            //Botao de aceitar o chamado
            binding.acceptButton.setOnClickListener {
                getCurrentMedicId { medicId ->
                    if (medicId != null) {
                        val requestRef =
                            emergencyRequest.id.let { it1 ->
                                FirebaseFirestore.getInstance().collection("emergency_requests").document(
                                    it1
                                )
                            }
                        //Checa se o numero maximo de medicos ja aceitou o chamado e abre o mesmo
                        requestRef?.get()?.addOnSuccessListener { documentSnapshot ->
                            val medics = documentSnapshot.get("medics") as? List<*>
                            if ((medics != null) && (medics.size < 5) && !medics.contains(medicId)) {
                                requestRef.update("medics", FieldValue.arrayUnion(medicId))
                                    .addOnSuccessListener {
                                        showPopup(emergencyRequest)
                                    }.addOnFailureListener {

                                        Toast.makeText(context, "Erro ao guardar o ID do medico no BD", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(context, "Maximo de medicos estourado ou nao foi possivel abrir o chamado", Toast.LENGTH_SHORT).show()
                            }
                        }?.addOnFailureListener {
                            Toast.makeText(context, "Erro ao buscar o documento", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "ID do medico nao disponivel", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            //Botao de recusar o chamado
            binding.declineButton.setOnClickListener {
                onDeclineClick(emergencyRequest)
            }
        }

        //Funcao que pega o ID do medico atual
        private fun getCurrentMedicId(callback: (String?) -> Unit) {
            val firebaseAuth = FirebaseAuth.getInstance()
            val currentUser = firebaseAuth.currentUser

            //Checa se o ID do usuario e nulo
            if (currentUser != null) {
                val currentUserId = currentUser.uid
                val usersCollection = FirebaseFirestore.getInstance().collection("users")
                val userDocument = usersCollection.document(currentUserId)

                //Pega o ID do medico logado (Document ID)
                userDocument.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            callback.invoke(documentSnapshot.id)
                        } else {
                            callback.invoke(null)
                        }
                    }
                    .addOnFailureListener {
                        callback.invoke(null)
                    }
            } else {
                callback.invoke(null)
            }
        }

        //Funcao que cuida do pop-up
        private fun showPopup(emergencyRequest: EmergencyRequest) {
            val dialog = Dialog(itemView.context)
            dialog.setContentView(R.layout.emergency_popup_dialog_fragment)

            // Inicializa as views
            val imageView1 = dialog.findViewById<ImageView>(R.id.imageView1)
            val imageView2 = dialog.findViewById<ImageView>(R.id.imageView2)
            val imageView3 = dialog.findViewById<ImageView>(R.id.imageView3)
            val descriptionTextView = dialog.findViewById<TextView>(R.id.descriptionTextView)
            val addressTextView = dialog.findViewById<TextView>(R.id.addressTextView)

            // Coloca a imagem do banco nas views
            Glide.with(itemView.context).load(emergencyRequest.imageUrl1).into(imageView1)
            Glide.with(itemView.context).load(emergencyRequest.imageUrl2).into(imageView2)
            Glide.with(itemView.context).load(emergencyRequest.imageUrl3).into(imageView3)
            descriptionTextView.text = emergencyRequest.description
            val address = "${emergencyRequest.street}, ${emergencyRequest.streetNumber}, ${emergencyRequest.city}"
            addressTextView.text = address


            dialog.show()
        }

    }
}

