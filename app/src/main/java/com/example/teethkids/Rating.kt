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

    private lateinit var pieChartView: PieChartView
    private lateinit var percentageTextView: TextView

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

                for (document in querySnapshot) {
                    val reviewText = document.getString("comment") ?: ""
                    val rating = document.getDouble("rating")?.toFloat() ?: 0.0f

                    totalRating += rating
                    maxRating += 5.0f

                    val review = Review(reviewText, rating, 5.0f)
                    reviews.add(review)
                }

                adapter.setData(reviews)
                updateAverageRating(totalRating, maxRating)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch reviews", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updateAverageRating(averageRating: Float, maxRating: Float) {
        val averageRatingTextView = view?.findViewById<TextView>(R.id.averageRatingTextView)
        averageRatingTextView?.text = "Average Rating: $averageRating"
    }
}
























