package com.example.teethkids

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.teethkids.databinding.ItemEmergencyRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class EmergencyRequestsAdapter(
    private val onItemClick: (EmergencyRequest) -> Unit,
    private val onDeclineClick: (EmergencyRequest) -> Unit
) : RecyclerView.Adapter<EmergencyRequestsAdapter.ViewHolder>() {

    private val emergencyRequests: MutableList<EmergencyRequest> = mutableListOf()
    private lateinit var context: Context

    // Responsavel por atualizar o data set do adapter com uma nova lista de objetos do "EmergencyRequest"
    fun setData(data: List<EmergencyRequest>) {
        emergencyRequests.clear()
        emergencyRequests.addAll(data)
        notifyDataSetChanged()
    }

    // Eh chamada pelo RecyclerView pra criar um novo ViewHolder para os itens da lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ItemEmergencyRequestBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // Esse metodo basicamente atualiza os conteudos do RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val emergencyRequest = emergencyRequests[position]
        holder.bind(emergencyRequest)
    }

    // Retorna o numero total de itens dentro do data set
    override fun getItemCount(): Int {
        return emergencyRequests.size
    }

    inner class ViewHolder(private val binding: ItemEmergencyRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Funcao de viewBinding
        fun bind(emergencyRequest: EmergencyRequest) {
            binding.titleTextView.text = emergencyRequest.title
            binding.descriptionTextView.text = emergencyRequest.description

            loadImages(emergencyRequest.imageUrls)

            // Botao de aceitar o chamado
            binding.acceptButton.setOnClickListener {
                showPopup(emergencyRequest)
            }
        }


        // Funcao que cuida do pop-up
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
            val address =
                "${emergencyRequest.street}, ${emergencyRequest.number}, ${emergencyRequest.city}"
            addressTextView.text = address

            dialog.show()
        }

        // Manda as imagens para os imageViews
        private fun loadImages(imageUrls: List<String>) {
            // Remove qualquer imageView ja existente
            binding.imageContainer.removeAllViews()

            // Carrega as urls das imagens em image views diferentes
            for (imageUrl in imageUrls) {
                val imageView = ImageView(context)
                imageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                binding.imageContainer.addView(imageView)

                // Usa o glide para carregar as imagens
                Glide.with(context).load(imageUrl).into(imageView)
            }
        }
    }
}

