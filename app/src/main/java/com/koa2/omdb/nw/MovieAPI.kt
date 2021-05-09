package com.koa2.omdb.nw

import com.koa2.omdb.apidata.MovieDetailItem
import com.koa2.omdb.apidata.MovieSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {

    @GET(".")
    suspend fun searchMovie(
        @Query("s") searchParam: String,
        @Query("apikey") apiKey: String,
        @Query("type") type: String = "movie",
    ): MovieSearchResponse

    @GET(".")
    suspend fun movieDetail(
        @Query("i") id: String,
        @Query("apikey") apiKey: String,
    ): MovieDetailItem
}