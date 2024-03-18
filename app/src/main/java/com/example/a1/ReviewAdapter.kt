package com.example.a1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class ReviewAdapter(private val reviewList: ArrayList<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.nametext)
        val stars: TextView = view.findViewById(R.id.ratings)
        val review: TextView = view.findViewById(R.id.reviewtext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.name.text = review.targetUserName
        holder.stars.text = starsRating(review.rating)
        holder.review.text=review.comment
    }
    fun starsRating(rating: Float): String {
        val roundedRating = rating.roundToInt()
        return "⭐".repeat(roundedRating)
    }



    override fun getItemCount() = reviewList.size
}