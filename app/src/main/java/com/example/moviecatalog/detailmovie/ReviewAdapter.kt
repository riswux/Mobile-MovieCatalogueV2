package com.example.moviecatalog.detailmovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecatalog.R
import com.example.moviecatalog.apiservice.response.MovieDetailResponse
import com.example.moviecatalog.convertServerDateToUserTimeZone
import com.example.moviecatalog.databinding.GenresItemBinding
import com.example.moviecatalog.databinding.ReviewsItemBinding

class ReviewAdapter(
    private val list: List<MovieDetailResponse.Review>,
    private val deleteReviewClick: DeleteReviewClick,
    private val editReviewClick: EditReviewClick,
    private val userId: String
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ReviewsItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ViewHolder {
        val binding = ReviewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ViewHolder, position: Int) {
        val review = list[list.size - (position + 1)]
        with(holder) {
            Glide.with(itemView).load(review.author.avatar)
                .placeholder(R.drawable.profile_placeholder).into(binding.userProfileImg)
            binding.usernameTxt.text = review.author.nickName
            binding.reviewTxt.text = review.reviewText
            binding.dateReviewTxt.text = convertServerDateToUserTimeZone(review.createDateTime)
            binding.ratingReviewTxt.text = review.rating.toString()

            if (userId != review.author.userId) {
                binding.editReviewCard.isVisible = false
                binding.deleteReviewCard.isVisible = false
            }

            binding.editReviewCard.setOnClickListener {
                editReviewClick.onEditClick(review)
            }

            binding.deleteReviewCard.setOnClickListener {
                deleteReviewClick.onDeleteClick(review.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}