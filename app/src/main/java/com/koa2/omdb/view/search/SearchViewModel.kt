package com.koa2.omdb.view.search

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koa2.omdb.AppConstants
import com.koa2.omdb.room.MovieBookMarkData
import com.koa2.omdb.usecase.BookMarkMovieUseCase
import com.koa2.omdb.usecase.SearchMovieUseCase
import com.koa2.omdb.view.data.MovieSearchResult
import com.koa2.omdb.view.data.MovieViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchMovieUseCase,
    private val bookMarkUseCase: BookMarkMovieUseCase,
) : ViewModel() {

    val searchInProgress = MutableLiveData<Boolean>()
    val searchMovieResult = MutableLiveData<List<MovieViewData>>()
    val searchMovieError = MutableLiveData<String>()

    val bookMarkUpdateLiveData = MutableLiveData<MovieViewData>()

    fun searchMovies(search: String?) {
        Log.d(TAG, "Search movies : $search")
        if (search.isNullOrEmpty()) {
            searchMovieError.postValue(EMPTY_SEARCH_TEXT)
        } else {
            searchInProgress.postValue(true)
            viewModelScope.launch {
                val moviesResult = searchUseCase.fetchMovies(search!!)
                searchInProgress.postValue(false)

                when (moviesResult) {
                    is MovieSearchResult.Success -> {
                        searchMovieResult.postValue(moviesResult.movieList)
                    }
                    is MovieSearchResult.NoResult -> {
                        searchMovieError.postValue(moviesResult.errorText)
                    }
                    is MovieSearchResult.Error -> {
                        searchMovieError.postValue(moviesResult.error.localizedMessage)
                    }
                    is MovieSearchResult.NoInternet -> {
                        searchMovieError.postValue(NO_INTERNET)
                    }
                }
            }
        }
    }

    fun onMovieBookMarkClick(movieItem: MovieViewData) {
        viewModelScope.launch {
            val data = MovieBookMarkData(movieId = movieItem.movieId,
                movieTitle = movieItem.title,
                moviePoster = movieItem.poster)
            try {
                bookMarkUseCase.toggleBookMark(data)
                bookMarkUpdateLiveData.postValue(movieItem.copy(isBookMarked = !movieItem.isBookMarked))
            } catch (e: Exception) {
                Log.e(TAG, "BookMark Toggle Failed", e)
            }
        }
    }

    companion object {
        const val TAG = AppConstants.TAG.plus("-SearchViewModel")
        const val NO_INTERNET = "No Internet"
        const val EMPTY_SEARCH_TEXT = "Empty Search Text"
    }
}