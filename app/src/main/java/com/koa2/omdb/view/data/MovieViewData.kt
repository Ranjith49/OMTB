package com.koa2.omdb.view.data

data class MovieViewData(
    val movieId: String,
    val title: String,
    val poster: String,
    val releaseDate: String,
    val runTime: String,
    val moviePlot: String,
    val genreList: String,
    val actorList: String,
    val director: String,
    val isBookMarked: Boolean,
)