package com.koa2.omdb.view.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.koa2.omdb.AppConstants
import com.koa2.omdb.R
import com.koa2.omdb.databinding.BookmarkMovieItemBinding
import com.koa2.omdb.room.MovieBookMarkData

class BookMarkMoviesAdapter : RecyclerView.Adapter<BookMarkMoviesAdapter.ViewHolder>() {

    private var movieList = mutableListOf<MovieBookMarkData>()

    inner class ViewHolder(private val binding: BookmarkMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: MovieBookMarkData) {
            binding.movieName.text = item.movieTitle

            // Load Image..
            Glide.with(binding.moviePoster)
                .load(item.moviePoster)
                .placeholder(R.drawable.default_movie_poster)
                .error(R.drawable.default_movie_poster)
                .into(binding.moviePoster)
        }
    }

    fun updateList(bookMarkList: List<MovieBookMarkData>) {
        this.movieList.clear()
        this.movieList.addAll(bookMarkList)
        notifyDataSetChanged()
    }

    companion object {
        const val TAG = AppConstants.TAG.plus("-BookMarkMovieAdapter")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BookmarkMovieItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = movieList[position]
        holder.bindItem(item)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}