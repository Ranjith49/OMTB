package com.koa2.omdb.apidata

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieSearchResponse(
    @Json(name = "Search")
    val moviesResult: List<MovieSearchItem>?,

    @Json(name = "totalResults")
    val totalResults: String?,

    @Json(name = "Error")
    val errorText: String?,

    @Json(name = "Response")
    val isOK: String
)