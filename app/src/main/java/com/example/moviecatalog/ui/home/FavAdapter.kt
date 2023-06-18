package com.example.moviecatalog.ui.home

import android.content.Intent
import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecatalog.detailmovie.DetailMovieActivity
import com.example.moviecatalog.R
import com.example.moviecatalog.apiservice.response.FavoritesResponse
import com.example.moviecatalog.databinding.FavoriteLayoutBinding

class FavAdapter(val list: List<FavoritesResponse.Movie>, val listener: DeleteFavClick): RecyclerView.Adapter<FavAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavAdapter.ViewHolder {
        val binding =  FavoriteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavAdapter.ViewHolder, position: Int) {
        val movie = list[position]

        with(holder) {
            Glide.with(itemView).load(movie.poster).placeholder(R.drawable.placeholder).into(binding.movieFavImg)
            binding.deleteFavCard.setOnClickListener {
                listener.onDeleteFavClick(movie.id)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailMovieActivity::class.java)
                intent.putExtra("MOVIE_ID", movie.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: FavoriteLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}