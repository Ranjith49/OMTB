package com.koa2.omdb.view.data

sealed class MovieSearchResult {
    data class Success(val movieList: List<MovieViewData>) : MovieSearchResult()
    data class Error(val error: Exception) : MovieSearchResult()
    data class NoResult(val errorText: String) : MovieSearchResult()
    object NoInternet : MovieSearchResult()
}