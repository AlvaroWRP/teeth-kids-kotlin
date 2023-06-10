package com.example.teethkids

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    private val reviews = mutableListOf<Review>()

    fun setData(data: List<Review>) {
        reviews.clear()
        reviews.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val commentTextView: TextView = itemView.findViewById(R.id.commentTextView)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)

        fun bind(review: Review) {
            commentTextView.text = review.reviewText
            ratingBar.rating = review.rating
            ratingBar.max = review.maxRating.toInt()
            ratingBar.stepSize = 1.0f
            ratingBar.progressTintList = ColorStateList.valueOf(Color.YELLOW)

            // Disable user interaction with the RatingBar
            ratingBar.setOnTouchListener { _, _ -> true }
            ratingBar.isFocusable = false
            ratingBar.isClickable = false
        }
    }
}
