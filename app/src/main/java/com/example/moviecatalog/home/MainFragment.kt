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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviecatalog.Preferences
import com.example.moviecatalog.R
import com.example.moviecatalog.databinding.FragmentMainBinding
import com.example.moviecatalog.login.LoginActivity

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null;
    private val binding get() = _binding!!

    lateinit var viewModel: MainViewModel

    var token = ""

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


    }

}