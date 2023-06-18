package com.example.moviecatalog.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecatalog.R
import com.example.moviecatalog.apiservice.response.MoviesResponse
import com.example.moviecatalog.databinding.GalleryLayoutBinding
import com.example.moviecatalog.detailmovie.DetailMovieActivity
import java.math.RoundingMode
import java.text.DecimalFormat

class GalleryAdapter(val context: Context) :
    PagingDataAdapter<MoviesResponse.Movie, GalleryAdapter.ViewHolder>(COMPARATOR) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        if (data != null) {
            holder.bindUI(data)

            holder.itemView.setOnClickListener {
                val intent = Intent(context, DetailMovieActivity::class.java)
                intent.putExtra("MOVIE_ID", data.id)
                context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            GalleryLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(private val binding: GalleryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindUI(data: MoviesResponse.Movie) {
            Glide.with(itemView).load(data.poster).placeholder(R.drawable.placeholder)
                .into(binding.movieCoverImg)
            binding.movieNameTxt.text = data.name
            binding.movieYearCountryTxt.text = "${data.year}.${data.country}"
            val genres = ArrayList<String>()
            for (i in data.genres) {
                genres.add(i.name)
            }

            binding.genreTxt.text = genres.joinToString(",")

            val ratings = ArrayList<Long>()
            for (i in data.reviews) {
                ratings.add(i.rating)
            }
            val totalRating = ratings.sum()
            val rating = (totalRating.toDouble() / ratings.size.toDouble())
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.CEILING

            binding.ratingTxt.text = df.format(rating)

            if(rating <= 2) {
                binding.ratingCard.setCardBackgroundColor(itemView.context.resources.getColor(R.color.red, null))
            } else if (rating <= 4) {
                binding.ratingCard.setCardBackgroundColor(itemView.context.resources.getColor(R.color.orange, null))
            } else if (rating <= 6) {
                binding.ratingCard.setCardBackgroundColor(itemView.context.resources.getColor(R.color.yellow, null))
            } else if (rating <= 8) {
                binding.ratingCard.setCardBackgroundColor(itemView.context.resources.getColor(R.color.green_spring, null))
            } else {
                binding.ratingCard.setCardBackgroundColor(itemView.context.resources.getColor(R.color.green, null))
            }

        }
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<MoviesResponse.Movie>() {
            override fun areContentsTheSame(
                oldItem: MoviesResponse.Movie,
                newItem: MoviesResponse.Movie
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: MoviesResponse.Movie,
                newItem: MoviesResponse.Movie
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}