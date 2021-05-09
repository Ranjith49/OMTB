package com.koa2.omdb.apidata

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailItem(
    @Json(name = "Title")
    val title: String,

    @Json(name = "Year")
    val year: String,

    @Json(name = "imdbID")
    val id: String,

    @Json(name = "Poster")
    val poster: String,

    @Json(name = "Released")
    val releaseDate: String,

    @Json(name = "Runtime")
    val runTime: String,

    @Json(name = "Plot")
    val plot: String,

    @Json(name = "Director")
    val director: String,

    @Json(name = "Actors")
    val actors: String,

    @Json(name = "Genre")
    val genre: String
)
