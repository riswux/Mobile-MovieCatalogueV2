package com.example.moviecatalog.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviecatalog.Preferences
import com.example.moviecatalog.R
import com.example.moviecatalog.apiservice.response.FavoritesResponse
import com.example.moviecatalog.databinding.FragmentMainBinding
import com.example.moviecatalog.detailmovie.DetailMovieActivity
import com.example.moviecatalog.login.LoginActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainFragment : Fragment(), DeleteFavClick {

    private var _binding: FragmentMainBinding? = null;
    private val binding get() = _binding!!

    lateinit var viewModel: MainViewModel

    var token = ""

    var galleryAdapter: GalleryAdapter? = null

    var movieId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val preferences = context?.let { Preferences(it) }

        token = "Bearer " + preferences?.getToken()

        binding.galleryRv.layoutManager = LinearLayoutManager(context)
        binding.favRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        galleryAdapter = context?.let { GalleryAdapter(it) }

        binding.galleryRv.adapter = galleryAdapter

        binding.watchBtn.setOnClickListener {
            if (movieId != null) {
                val intent = Intent(context, DetailMovieActivity::class.java)
                intent.putExtra("MOVIE_ID", movieId)
                startActivity(intent)
            }
        }

        galleryAdapter?.addLoadStateListener {
            if (it.source.refresh is LoadState.NotLoading) {
                binding.progressBar4.isVisible = false
                binding.mainLay.isVisible = true
            } else {
                binding.progressBar4.isVisible = true
                binding.mainLay.isVisible = false
            }
        }

        viewModel.favData.observe(viewLifecycleOwner) {
            if (it is FavoritesResponse) {
                if (it.movies.isNotEmpty()) {
                    binding.favRv.adapter = FavAdapter(it.movies, this)
                    binding.favTxt.isVisible = true
                    binding.favRv.isVisible = true
                } else {
                    binding.favTxt.isVisible = false
                    binding.favRv.isVisible = false
                }
            } else {
                if (it.toString() == "401") {
                    preferences?.deleteToken()
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }

        viewModel.moviesData.observe(viewLifecycleOwner){
            val bannerMovie = it.movies.first()
            Glide.with(view).load(bannerMovie.poster).placeholder(R.drawable.placeholder).into(binding.mainMovieImg)
            movieId = bannerMovie.id
        }

        viewModel.deleteFavData.observe(viewLifecycleOwner) {
            viewModel.getFavoriteMovie(token)
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDeleteFavClick(id: String) {
        viewModel.deleteFavMovie(id, token)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {

            viewModel.getFavoriteMovie(token)
            viewModel.getMovies()

            viewModel.movies.collectLatest {
                galleryAdapter?.submitData(it)
            }
        }
    }
}