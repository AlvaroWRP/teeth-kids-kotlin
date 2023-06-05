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

        pieChartView = view.findViewById(R.id.reviewsPieChart)
        percentageTextView = view.findViewById(R.id.percentageTextView)
    }

    private fun fetchReviews() {
        db.collection("reviews")
            .whereEqualTo("medic", medicId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val reviews = mutableListOf<Review>()
                var goodCount = 0
                var badCount = 0

                for (document in querySnapshot) {
                    val reviewText = document.getString("review") ?: ""
                    val isGood = document.getBoolean("isGood") ?: false
                    val review = Review(reviewText, isGood)
                    reviews.add(review)

                    if (isGood) {
                        goodCount++
                    } else {
                        badCount++
                    }
                }

                adapter.setData(reviews)
                updatePieChart(goodCount, badCount)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch reviews", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updatePieChart(goodCount: Int, badCount: Int) {
        val totalReviews = goodCount + badCount

        val goodPercentage = if (totalReviews > 0) goodCount.toFloat() / totalReviews else 0f
        val badPercentage = if (totalReviews > 0) badCount.toFloat() / totalReviews else 0f

        pieChartView.setPercentage(goodPercentage, badPercentage)

        val goodPercentageText = "${(goodPercentage * 100).toInt()}%"
        val badPercentageText = "${(badPercentage * 100).toInt()}%"

        val percentageText = "$goodPercentageText / $badPercentageText"
        percentageTextView.text = percentageText
    }
}






















