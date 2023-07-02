package com.example.moviecatalog.detailmovie

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.moviecatalog.Preferences
import com.example.moviecatalog.R
import com.example.moviecatalog.apiservice.body.AddReviewBody
import com.example.moviecatalog.apiservice.response.FavoritesResponse
import com.example.moviecatalog.apiservice.response.MovieDetailResponse
import com.example.moviecatalog.databinding.ActivityDetailMovieBinding
import com.example.moviecatalog.databinding.AddReviewLayoutBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.text.NumberFormat
import java.util.Currency


class DetailMovieActivity : AppCompatActivity(), DeleteReviewClick, EditReviewClick {

    lateinit var binding: ActivityDetailMovieBinding

    private val viewModel: DetailMovieViewModel by viewModels()

    var movieId = ""
    var token = ""
    var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieId = intent.getStringExtra("MOVIE_ID") ?: ""

        val preferences = Preferences(this)
        token = "Bearer " + preferences.getToken()

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW_REVERSE
        layoutManager.justifyContent = JustifyContent.FLEX_END

        binding.genresRv.layoutManager = layoutManager
        binding.reviewRv.layoutManager = LinearLayoutManager(this)
        binding.genresRv.isNestedScrollingEnabled = false
        binding.reviewRv.isNestedScrollingEnabled = false

        viewModel.getProfile(token)

        viewModel.userData.observe(this) {
            userId = it.id
            viewModel.getDetailMovie(movieId)
        }

        val ids = ArrayList<String>()

        viewModel.favData.observe(this) {
            ids.clear()
            if (it is FavoritesResponse) {
                if (it.movies.isNotEmpty()) {
                    for (i in it.movies) {
                        ids.add(i.id)
                    }
                }

                if (ids.contains(movieId)) {
                    binding.favImg.setImageResource(R.drawable.baseline_favorite_24)
                } else {
                    binding.favImg.setImageResource(R.drawable.baseline_favorite_fill)
                }
            } else {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getLoading.observe(this) {
            binding.progressBar3.isVisible = it
            binding.scrollView.isVisible = !it
        }

        viewModel.detailData.observe(this) { response ->

            viewModel.getFavoriteMovie(token)

            Glide.with(this).load(response.poster).placeholder(R.drawable.placeholder).into(binding.movieDetailCoverImg)
            binding.movieDetailNameTxt.text = response.name
            binding.movieDescTxt.text = response.description
            binding.yearTxt.text = response.year.toString()
            binding.countryTxt.text = response.country
            binding.durationTxt.text = response.time.toString()
            binding.directorTxt.text = response.director
            binding.budgetTxt.text = convertCurrency(response.budget.toInt())
            binding.feesTxt.text = convertCurrency(response.budget.toInt())
            binding.ageLimitTxt.text = response.ageLimit.toString() + "+"

            binding.addReviewImg.isVisible = response.reviews.none { it.author.userId == userId }

            binding.genresRv.adapter = GenreAdapter(response.genres)
            binding.reviewRv.adapter = ReviewAdapter(response.reviews, this, this, userId)

        }

        viewModel.editReviewData.observe(this) {
            viewModel.getDetailMovie(movieId)
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
        viewModel.deleteReviewData.observe(this) {
            viewModel.getDetailMovie(movieId)
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

        viewModel.addReviewData.observe(this) {
            viewModel.getDetailMovie(movieId)
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

        viewModel.addFavData.observe(this) {
            viewModel.getFavoriteMovie(token)
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.addReviewImg.setOnClickListener {
            val dialog = Dialog(this)
            val binding = AddReviewLayoutBinding.inflate(layoutInflater)
            dialog.setContentView(binding.root)
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false)
            binding.saveBtn.setOnClickListener {

                val reviewt = binding.reviewEdt.text.toString()
                val rating = binding.ratingBar.rating.toInt()
                val isAnonymous = binding.anonymousCheck.isChecked

                val body = AddReviewBody(
                    reviewt,
                    rating = rating,
                    isAnonymous
                )

                if (reviewt.isNotBlank()) {
                    viewModel.addReview(token, movieId, body)
                    dialog.dismiss()
                }
            }

            binding.cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        binding.favCard.setOnClickListener {
            viewModel.addFavorite(token, movieId)
             binding.favImg.setImageResource(R.drawable.baseline_favorite_24)
        }

        binding.backCard.setOnClickListener {
               onBackPressed()
        }

    }

    override fun onDeleteClick(id: String) {
        viewModel.deleteReview(token, id, movieId)
    }

    override fun onEditClick(review: MovieDetailResponse.Review) {
        val dialog = Dialog(this)
        val binding = AddReviewLayoutBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false)

        binding.ratingBar.rating = review.rating.toFloat()
        binding.reviewEdt.setText(review.reviewText)
        binding.anonymousCheck.isChecked = review.isAnonymous

        binding.saveBtn.setOnClickListener {

            val reviewt = binding.reviewEdt.text.toString()
            val rating = binding.ratingBar.rating.toInt()
            val isAnonymous = binding.anonymousCheck.isChecked

            val body = AddReviewBody(
                reviewt,
                rating = rating,
                isAnonymous
            )

            viewModel.editReview(token, review.id, movieId, body)
            dialog.dismiss()
        }

        binding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun convertCurrency(value: Int): String {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("USD")

        return format.format(value)
    }
}