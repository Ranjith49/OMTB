package com.koa2.omdb.view.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.koa2.omdb.AppConstants
import com.koa2.omdb.databinding.FragmentSearchBinding
import com.koa2.omdb.view.MoviesViewModel
import com.koa2.omdb.view.data.MovieViewData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMovieFragment : Fragment(), MovieOnClickListener {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val moviesViewModel: MoviesViewModel by activityViewModels()
    private var adapter: SearchMoviesAdapter = SearchMoviesAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Log.d(TAG, "OnCreate View")
        binding = FragmentSearchBinding.inflate(LayoutInflater.from(context))
        binding.searchMovie.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "User Enter String : $query")
                searchViewModel.searchMovies(query)
                return false
            }
        })

        binding.movieList.layoutManager = LinearLayoutManager(context)
        binding.movieList.itemAnimator = DefaultItemAnimator()
        binding.movieList.adapter = adapter

        initListeners()
        return binding.root
    }

    private fun initListeners() {
        searchViewModel.searchInProgress.observe(viewLifecycleOwner, {
            Log.d(TAG, "On Movie Progress Update :  $it")
            if (it) {
                binding.searchMovie.visibility = View.GONE
                binding.movieList.visibility = View.GONE
                binding.searchProgress.visibility = View.VISIBLE
            } else {
                binding.searchMovie.visibility = View.VISIBLE
                binding.searchProgress.visibility = View.GONE
            }
        })
        searchViewModel.searchMovieError.observe(viewLifecycleOwner, {
            Log.d(TAG, "On Movie Search Error")
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        searchViewModel.searchMovieResult.observe(viewLifecycleOwner, {
            Log.d(TAG, "On Movie Search finished")
            setUpList(it)
        })
        searchViewModel.bookMarkUpdateLiveData.observe(viewLifecycleOwner, {
            Log.d(TAG, "on BookMark Update finishes")
            updateBookMark(it)
        })
    }

    private fun setUpList(moviesList: List<MovieViewData>) {
        adapter.updateList(moviesList)
        binding.movieList.visibility = View.VISIBLE
    }

    private fun updateBookMark(movieItem: MovieViewData) {
        adapter.updateBookMarkStatus(movieItem.movieId, movieItem.isBookMarked)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "On Resume")
        binding.searchMovie.requestFocus()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "On Pause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "On Destroy View")
    }

    companion object {
        const val TAG = AppConstants.TAG.plus("-SearchFrag")
        fun newInstance() = SearchMovieFragment()
    }

    override fun onMovieClick(movieItem: MovieViewData) {
        moviesViewModel.onMovieClick(movieItem)
    }

    override fun onMovieBookMarkClick(movieItem: MovieViewData) {
        searchViewModel.onMovieBookMarkClick(movieItem)
    }
}