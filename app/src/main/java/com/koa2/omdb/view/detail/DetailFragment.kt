package com.koa2.omdb.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.koa2.omdb.AppConstants
import com.koa2.omdb.R
import com.koa2.omdb.databinding.FramentDetailBinding
import com.koa2.omdb.view.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FramentDetailBinding
    private val moviesViewModel: MoviesViewModel by activityViewModels()
    private var movieId: String? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        movieId = arguments?.getString(MOVIE_ID_KEY) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FramentDetailBinding.inflate(LayoutInflater.from(context))
        setMovieData()
        return binding.root
    }

    private fun setMovieData() {
        moviesViewModel.movieClickLiveData.value?.let {
            binding.movieName.text = it.title
            binding.moviePlot.text = it.moviePlot
            binding.movieDirector.text = it.director
            binding.movieRuntime.text = it.runTime
            binding.movieTime.text = it.releaseDate
            binding.movieGenre.text = it.genreList
            binding.movieActor.text = it.actorList

            Glide.with(binding.moviePoster)
                .load(it.poster)
                .placeholder(R.drawable.default_movie_poster)
                .error(R.drawable.default_movie_poster)
                .into(binding.moviePoster)
        }
    }

    companion object {
        const val TAG = AppConstants.TAG.plus("-DetailFrag")
        const val MOVIE_ID_KEY = "movieId"

        fun newInstance(movieId: String) = DetailFragment().apply {
            arguments = bundleOf(MOVIE_ID_KEY to movieId)
        }
    }
}