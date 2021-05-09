package com.koa2.omdb.view.search

import com.koa2.omdb.view.data.MovieViewData

interface MovieOnClickListener {

    fun onMovieBookMarkClick(movieItem: MovieViewData)

    fun onMovieClick(movieItem: MovieViewData)
}