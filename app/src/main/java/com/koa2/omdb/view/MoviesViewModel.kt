package com.koa2.omdb.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.koa2.omdb.AppConstants
import com.koa2.omdb.view.data.MovieViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor() : ViewModel() {

    val movieClickLiveData = MutableLiveData<MovieViewData>()

    fun onMovieClick(item: MovieViewData) {
        Log.d(TAG, "On Movie Click : ${item.movieId}")
        movieClickLiveData.postValue(item)
    }

    companion object {
        const val TAG = AppConstants.TAG.plus("-MoviesViewModel")
    }
}