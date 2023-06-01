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
import com.example.moviecatalog.databinding.FragmentMainBinding
import com.example.moviecatalog.ui.home.GalleryAdapter
import com.example.moviecatalog.login.LoginActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
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

        galleryAdapter?.addLoadStateListener {
            if (it.source.refresh is LoadState.NotLoading) {
                binding.progressBar4.isVisible = false
                binding.mainLay.isVisible = true
            } else {
                binding.progressBar4.isVisible = true
                binding.mainLay.isVisible = false
            }
        }

        viewModel.moviesData.observe(viewLifecycleOwner){
            val bannerMovie = it.movies.first()
            Glide.with(view).load(bannerMovie.poster).placeholder(R.drawable.placeholder).into(binding.mainMovieImg)
            movieId = bannerMovie.id
        }

    }
    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.getMovies()

            viewModel.movies.collectLatest {
                galleryAdapter?.submitData(it)
            }
        }
    }

}