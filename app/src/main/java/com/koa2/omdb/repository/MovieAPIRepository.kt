package com.koa2.omdb.repository

import com.koa2.omdb.BuildConfig
import com.koa2.omdb.apidata.MovieDetailItem
import com.koa2.omdb.apidata.MovieSearchResponse
import com.koa2.omdb.nw.MovieAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieAPIRepository @Inject constructor(private val movieAPI: MovieAPI) {

    suspend fun getMovies(search: String): MovieSearchResponse {
        return movieAPI.searchMovie(search, BuildConfig.API_KEY)
    }

    suspend fun getMovieDetail(movieId: String): MovieDetailItem {
        return movieAPI.movieDetail(movieId, BuildConfig.API_KEY)
    }
}