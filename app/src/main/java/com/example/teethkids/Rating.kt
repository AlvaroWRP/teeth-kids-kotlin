package com.example.teethkids

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Rating : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ReviewAdapter

    private var medicId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance() // Inicializa o FirebaseAuth
        db = FirebaseFirestore.getInstance() // Inicializa o FirebaseFirestore
        medicId = auth.currentUser?.uid ?: "" // Obtém o ID do médico atualmente logado ou atribui uma string vazia se não houver usuário logado
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_rating, container, false) // Infla o layout do fragmento
        setupViews(view) // Configura as visualizações
        fetchReviews() // Obtém as avaliações
        return view // Retorna a visualização do fragmento
    }

    private fun setupViews(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView) // Obtém a RecyclerView do layout
        adapter = ReviewAdapter() // Inicializa o adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Define o layout manager da RecyclerView
        recyclerView.adapter = adapter // Define o adapter da RecyclerView
    }

    private fun fetchReviews() {
        db.collection("dentists_ratings") // Obtém a coleção "dentists_ratings" do Firestore
            .document(medicId) // Obtém o documento com o ID do médico atual
            .collection("ratings") // Obtém a subcoleção "ratings" dentro do documento do médico
            .get() // Realiza a consulta assíncrona para obter os documentos
            .addOnSuccessListener { querySnapshot ->
                val reviews = mutableListOf<Review>() // Lista para armazenar as avaliações
                var totalRating = 0.0f // Variável para calcular a soma total das notas
                var maxRating = 0.0f // Variável para calcular a soma máxima das notas
                var reviewCount = 0 // Variável para contar o número de avaliações

                for (document in querySnapshot) {
                    val reviewText = document.getString("comment") ?: "" // Obtém o texto da avaliação do documento
                    val rating = document.getDouble("rating")?.toFloat() ?: 0.0f // Obtém a nota da avaliação do documento

                    totalRating += rating // Soma a nota à soma total das notas
                    maxRating += 5.0f // Adiciona 5 à soma máxima das notas (considerando que a nota máxima é 5)
                    reviewCount++ // Incrementa o contador de avaliações

                    val review = Review(reviewText, rating, 5.0f) // Cria um objeto Review com os dados da avaliação
                    reviews.add(review) // Adiciona a avaliação à lista de avaliações
                }

                adapter.setData(reviews) // Define a lista de avaliações no adapter da RecyclerView
                updateAverageRating(totalRating, maxRating, reviewCount) // Atualiza a exibição da nota média
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Falha ao receber as avaliacoes", Toast.LENGTH_SHORT)
                    .show() // Exibe uma mensagem de erro em caso de falha ao obter as avaliações
            }
    }

    private fun updateAverageRating(totalRating: Float, maxRating: Float, reviewCount: Int) {
        val averageRating = if (reviewCount > 0) totalRating / reviewCount else 0.0f // Calcula a nota média se houver avaliações, caso contrário, define como 0.0
        val averageRatingTextView = view?.findViewById<TextView>(R.id.averageRatingTextView) // Obtém a TextView para exibir a nota média
        averageRatingTextView?.text = "Sua nota: $averageRating" // Atualiza o texto da TextView com a nota média calculada
    }
}

