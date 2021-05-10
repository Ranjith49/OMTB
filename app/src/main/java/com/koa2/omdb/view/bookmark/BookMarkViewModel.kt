package com.koa2.omdb.view.bookmark

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koa2.omdb.AppConstants
import com.koa2.omdb.room.MovieBookMarkData
import com.koa2.omdb.usecase.BookMarkMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(private val bookMarkMovieUseCase: BookMarkMovieUseCase) :
    ViewModel() {

    val bookMarksData = MutableLiveData<List<MovieBookMarkData>>()

    init {
        viewModelScope.launch {
            bookMarkMovieUseCase.getAllBookMarks().collect {
                bookMarksData.postValue(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "on Cleared")
    }

    companion object {
        const val TAG = AppConstants.TAG.plus("-BookMarkViewModel")
    }
}