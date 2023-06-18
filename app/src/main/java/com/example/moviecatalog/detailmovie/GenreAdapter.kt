package com.example.moviecatalog.detailmovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalog.apiservice.response.MovieDetailResponse
import com.example.moviecatalog.databinding.GenresItemBinding

class GenreAdapter(private val list: List<MovieDetailResponse.Genre>) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: GenresItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreAdapter.ViewHolder {
        val binding = GenresItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreAdapter.ViewHolder, position: Int) {
        val genre = list[position]
        with(holder) {
            binding.genreTxt.text = genre.name
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}