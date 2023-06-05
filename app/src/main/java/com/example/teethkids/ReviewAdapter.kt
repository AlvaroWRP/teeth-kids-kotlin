package com.example.teethkids

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private val reviews = mutableListOf<Review>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int = reviews.size

    fun setData(reviews: List<Review>) {
        this.reviews.clear()
        this.reviews.addAll(reviews)
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val reviewTextView: TextView = itemView.findViewById(R.id.reviewTextView)
        private val likeImageView: ImageView = itemView.findViewById(R.id.likeImageView)
        private val dislikeImageView: ImageView = itemView.findViewById(R.id.dislikeImageView)

        fun bind(review: Review) {
            reviewTextView.text = review.reviewText
            if (review.isGood) {
                likeImageView.visibility = View.VISIBLE
                dislikeImageView.visibility = View.GONE
            } else {
                likeImageView.visibility = View.GONE
                dislikeImageView.visibility = View.VISIBLE
            }
        }
    }
}





