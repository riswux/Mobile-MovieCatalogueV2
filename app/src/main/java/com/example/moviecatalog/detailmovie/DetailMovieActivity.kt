package com.example.moviecatalog.detailmovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviecatalog.databinding.ActivityDetailMovieBinding

class DetailMovieActivity : AppCompatActivity() {

     lateinit var binding: ActivityDetailMovieBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}