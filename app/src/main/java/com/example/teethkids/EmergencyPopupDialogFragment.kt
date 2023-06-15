package com.example.teethkids

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.teethkids.databinding.EmergencyPopupDialogFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class EmergencyPopupDialogFragment : DialogFragment() {
    private lateinit var binding: EmergencyPopupDialogFragmentBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var onDeclineClick: (() -> Unit)? = null

    //Usado para inflar o layout do popup
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EmergencyPopupDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura as visualizações do diálogo e lida com os cliques dos botões
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emergencyRequest: EmergencyRequest? = arguments?.getParcelable("emergencyRequest")

        emergencyRequest?.let { request ->
            // Pega as fotos do banco e as mostra nas imageViews
            loadImage(request.imageUrl1, binding.imageView1)
            loadImage(request.imageUrl2, binding.imageView2)
            loadImage(request.imageUrl3, binding.imageView3)

            // Pega a descricao do chamado e o mostra na TextView
            binding.descriptionTextView.text = request.description

            // Pega o endereco formatado e mostra ele no TextView
            val address = buildAddressString(request.street, request.number, request.city)
            binding.addressTextView.text = address

            // Fecha o Pop up
            binding.declineButton.setOnClickListener {
                onDeclineClick?.invoke()
                dismiss()
            }

            /* Checa se o ID do medico eh nulo, se nao, checa se o limite de 5 medicos foi
            atingido e/ou se o medico ja aceitou este chamado antes,
            se nao, vai para a tela que encerra o atendimento*/
            binding.acceptButton.setOnClickListener {
                getCurrentMedicId { medicId ->
                    if (medicId != null) {
                        val requestRef = db.collection("emergency_requests").document(request.id)

                        requestRef.get().addOnSuccessListener { documentSnapshot ->
                            val medics = documentSnapshot.get("medics") as? List<*>
                            if ((medics != null) && (medics.size < 5) && !medics.contains(medicId)) {
                                requestRef.update("medics", FieldValue.arrayUnion(medicId))
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "Chamado aceito, vá ao local da ocorrência!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dismiss()
                                        val intent =
                                            Intent(context, EndEmergencyActivity::class.java)
                                        startActivity(intent)
                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "Erro ao guardar o ID do médico no BD",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Número máximo de médicos atingido ou não foi possível abrir o chamado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Erro ao buscar o documento",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "ID do médico não disponível",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }

    // Obtem o ID do médico atualmente logado no aplicativo
    private fun getCurrentMedicId(callback: (String?) -> Unit) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        // Checa se o ID eh nulo
        if (currentUser != null) {
            val currentUserId = currentUser.uid
            val usersCollection = db.collection("users")
            val userDocument = usersCollection.document(currentUserId)

            // pega o document ID medico logado
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

    // Usado para carregar uma imagem de uma URL usando a biblioteca Picasso
    private fun loadImage(imageUrl: String?, imageView: ImageView) {
        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(imageView)
        }
    }

    // Usado para construir uma string contendo o endereço formatado com base nos parâmetros de rua, número e cidade
    private fun buildAddressString(street: String?, number: String?, city: String?): String {
        val addressBuilder = StringBuilder()
        if (!street.isNullOrEmpty()) {
            addressBuilder.append(street)
        }
        if (!number.isNullOrEmpty()) {
            addressBuilder.append(", $number")
        }
        if (!city.isNullOrEmpty()) {
            addressBuilder.append(", $city")
        }
        return addressBuilder.toString()
    }

    /* A classe possui um objeto companheiro companion object que contém um método newInstance usado
    para criar uma instância do EmergencyPopupDialogFragment com base em um objeto EmergencyRequest.
    Esse método recebe um objeto EmergencyRequest como parâmetro, extrai os valores relevantes e os
    armazena em um Bundle que é então usado para configurar os argumentos do fragmento */
    companion object {
        private const val ARG_IMAGE_URL_1 = "image_url_1"
        private const val ARG_IMAGE_URL_2 = "image_url_2"
        private const val ARG_IMAGE_URL_3 = "image_url_3"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_STREET = "street"
        private const val ARG_STREET_NUMBER = "number"
        private const val ARG_CITY = "city"

        fun newInstance(emergencyRequest: EmergencyRequest): EmergencyPopupDialogFragment {
            val args = Bundle().apply {
                putString(ARG_IMAGE_URL_1, emergencyRequest.imageUrl1)
                putString(ARG_IMAGE_URL_2, emergencyRequest.imageUrl2)
                putString(ARG_IMAGE_URL_3, emergencyRequest.imageUrl3)
                putString(ARG_DESCRIPTION, emergencyRequest.description)
                putString(ARG_STREET, emergencyRequest.street)
                putString(ARG_STREET_NUMBER, emergencyRequest.number)
                putString(ARG_CITY, emergencyRequest.city)
            }
            val fragment = EmergencyPopupDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
