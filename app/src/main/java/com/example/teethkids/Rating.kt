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
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        medicId = auth.currentUser?.uid ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_rating, container, false)
        setupViews(view)
        fetchReviews()
        return view
    }

    private fun setupViews(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ReviewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun fetchReviews() {
        db.collection("dentists_ratings")
            .document(medicId)
            .collection("ratings")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val reviews = mutableListOf<Review>()
                var totalRating = 0.0f
                var maxRating = 0.0f
                var reviewCount = 0

                for (document in querySnapshot) {
                    val reviewText = document.getString("comentario") ?: ""
                    val rating = document.getDouble("avaliacao")?.toFloat() ?: 0.0f

                    totalRating += rating
                    maxRating += 5.0f
                    reviewCount++

                    val review = Review(reviewText, rating, 5.0f)
                    reviews.add(review)
                }

                adapter.setData(reviews)
                updateAverageRating(totalRating, maxRating, reviewCount)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Falha ao receber as avaliacoes", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun updateAverageRating(totalRating: Float, maxRating: Float, reviewCount: Int) {
        val averageRating = if (reviewCount > 0) totalRating / reviewCount else 0.0f
        val averageRatingTextView = view?.findViewById<TextView>(R.id.averageRatingTextView)
        averageRatingTextView?.text = "Sua nota: $averageRating"
    }
}
