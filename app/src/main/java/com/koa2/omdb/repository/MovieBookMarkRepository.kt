package com.koa2.omdb.repository

import com.koa2.omdb.room.MovieBookMarkDao
import com.koa2.omdb.room.MovieBookMarkData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieBookMarkRepository @Inject constructor(private val movieBookMarkDao: MovieBookMarkDao) {

    suspend fun isBookMarked(movieId: String): Boolean {
        val id = movieBookMarkDao.isBookMarked(movieId)
        return !id.isNullOrEmpty()
    }

    suspend fun insertMovie(movieData: MovieBookMarkData) {
        return movieBookMarkDao.insertMovie(movieData)
    }

    suspend fun deleteMovie(movieId: String) {
        return movieBookMarkDao.deleteMovie(movieId)
    }

    fun getAllBookMarks(): Flow<List<MovieBookMarkData>> {
        return movieBookMarkDao.getAllMovies()
    }
}