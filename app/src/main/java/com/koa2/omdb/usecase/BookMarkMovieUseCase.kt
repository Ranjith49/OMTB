package com.koa2.omdb.usecase

import com.koa2.omdb.repository.MovieBookMarkRepository
import com.koa2.omdb.room.MovieBookMarkData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookMarkMovieUseCase @Inject constructor(private val bookMarkRepository: MovieBookMarkRepository) {

    fun getAllBookMarks(): Flow<List<MovieBookMarkData>> {
        return bookMarkRepository.getAllBookMarks()
    }

    suspend fun toggleBookMark(data: MovieBookMarkData) {
        val isBookMarked = bookMarkRepository.isBookMarked(data.movieId)
        if (isBookMarked) {
            bookMarkRepository.deleteMovie(data.movieId)
        } else {
            bookMarkRepository.insertMovie(data)
        }
    }
}