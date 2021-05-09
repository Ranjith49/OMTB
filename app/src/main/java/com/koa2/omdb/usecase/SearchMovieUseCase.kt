package com.koa2.omdb.usecase

import com.koa2.omdb.NwConnectivityInfo
import com.koa2.omdb.repository.MovieAPIRepository
import com.koa2.omdb.repository.MovieBookMarkRepository
import com.koa2.omdb.view.data.MovieSearchResult
import com.koa2.omdb.view.data.MovieViewData
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(
    private val nwConnectivityInfo: NwConnectivityInfo,
    private val apiRepository: MovieAPIRepository,
    private val bookMarkRepository: MovieBookMarkRepository,
) {

    companion object {
        const val ERROR_RESPONSE = "False"
        const val UNKNOWN_ERROR_MSG = "Unknown Error. Try Again"
    }

    /**
     * Method to fetch movies based on a search string
     */
    suspend fun fetchMovies(search: String): MovieSearchResult {
        if (!nwConnectivityInfo.isConnectedToInternet()) {
            return MovieSearchResult.NoInternet
        }

        // Actual API call ..
        return withContext(Dispatchers.IO) {
            val movieList = mutableListOf<MovieViewData>()
            try {
                val searchResult = apiRepository.getMovies(search)
                if (searchResult.isOK == ERROR_RESPONSE) {
                    return@withContext MovieSearchResult.NoResult(searchResult.errorText
                        ?: UNKNOWN_ERROR_MSG)
                }
                searchResult.moviesResult?.let {
                    for (searchItem in it) {
                        val item = apiRepository.getMovieDetail(searchItem.id)
                        val isBookMarked = bookMarkRepository.isBookMarked(searchItem.id)
                        val viewData = MovieViewData(
                            item.id,
                            item.title,
                            item.poster,
                            item.releaseDate,
                            item.runTime,
                            item.plot,
                            item.genre,
                            item.actors,
                            item.director,
                            isBookMarked
                        )
                        movieList.add(viewData)
                    }
                }
                MovieSearchResult.Success(movieList)
            } catch (e: Exception) {
                MovieSearchResult.Error(e)
            }
        }
    }
}