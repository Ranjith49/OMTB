package com.koa2.omdb.view.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.koa2.omdb.AppConstants
import com.koa2.omdb.R
import com.koa2.omdb.databinding.SearchMovieItemBinding
import com.koa2.omdb.view.data.MovieViewData

class SearchMoviesAdapter(val onClickListener: MovieOnClickListener) :
    RecyclerView.Adapter<SearchMoviesAdapter.SearchMovieHolder>() {

    private var moviesList: MutableList<MovieViewData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieHolder {
        val binding = SearchMovieItemBinding.inflate(LayoutInflater.from(parent.context))
        return SearchMovieHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchMovieHolder, position: Int) {
        val item = moviesList[position]
        holder.bindItem(item)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    fun updateList(list: List<MovieViewData>) {
        this.moviesList.clear()
        this.moviesList.addAll(list)
        notifyDataSetChanged()
    }

    fun updateBookMarkStatus(movieId: String, bookMark: Boolean) {
        Log.d(TAG, "update status : $movieId BookMark :$bookMark")
        val item = this.moviesList.find {
            it.movieId == movieId
        }
        item?.let {
            val index = moviesList.indexOf(it)
            moviesList[index] = it.copy(isBookMarked = bookMark)
            notifyDataSetChanged()
        }
    }

    inner class SearchMovieHolder(private val binding: SearchMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: MovieViewData) {
            binding.movieName.text = item.title
            binding.movieYear.text = if (item.releaseDate == NOT_AVAILABLE) "" else item.releaseDate
            binding.movieDirector.text = if (item.director == NOT_AVAILABLE) "" else item.director
            binding.moviePlot.text = if (item.moviePlot == NOT_AVAILABLE) "" else item.moviePlot

            // Load Image..
            Glide.with(binding.moviePoster)
                .load(item.poster)
                .placeholder(R.drawable.default_movie_poster)
                .error(R.drawable.default_movie_poster)
                .into(binding.moviePoster)

            if (item.isBookMarked) {
                binding.movieBookmark.setImageResource(R.drawable.bookmarked)
            } else {
                binding.movieBookmark.setImageResource(R.drawable.not_bookmarked)
            }

            // Click Listener ..
            binding.root.setOnClickListener {
                onClickListener.onMovieClick(item)
            }
            binding.movieBookmark.setOnClickListener {
                onClickListener.onMovieBookMarkClick(item)
            }
        }
    }

    companion object {
        const val TAG = AppConstants.TAG.plus("-SearchMovieAdapter")
        const val NOT_AVAILABLE = "N/A"
    }
}