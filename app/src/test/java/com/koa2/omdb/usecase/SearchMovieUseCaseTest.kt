package com.koa2.omdb.usecase

import com.koa2.omdb.NwConnectivityInfo
import com.koa2.omdb.apidata.MovieDetailItem
import com.koa2.omdb.apidata.MovieSearchItem
import com.koa2.omdb.apidata.MovieSearchResponse
import com.koa2.omdb.repository.MovieAPIRepository
import com.koa2.omdb.repository.MovieBookMarkRepository
import com.koa2.omdb.view.data.MovieSearchResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
class SearchMovieUseCaseTest {

    @Mock
    private lateinit var nwConnectivityInfo: NwConnectivityInfo

    @Mock
    private lateinit var apiRepository: MovieAPIRepository

    @Mock
    private lateinit var bookMarkRepository: MovieBookMarkRepository

    private lateinit var movieUseCase: SearchMovieUseCase

    private val firstMovie = MovieSearchItem("Fast & Furious 6",
        "2013",
        "tt1905041",
        "https://m.media-amazon.com/images/M/MV5BMTM3NTg2NDQzOF5BMl5BanBnXkFtZTcwNjc2NzQzOQ@@._V1_SX300.jpg")
    private val secondMovie = MovieSearchItem("Fast Five",
        "2011",
        "tt1596343",
        "https://m.media-amazon.com/images/M/MV5BMTUxNTk5MTE0OF5BMl5BanBnXkFtZTcwMjA2NzY3NA@@._V1_SX300.jpg")
    private val firstMovieDetail = MovieDetailItem(
        "Fast & Furious 6",
        "2013",
        "tt1905041",
        "https://m.media-amazon.com/images/M/MV5BMTM3NTg2NDQzOF5BMl5BanBnXkFtZTcwNjc2NzQzOQ@@._V1_SX300.jpg",
        "24 May 2013",
        "130 min",
        "Hobbs has Dominic and Brian reassemble their crew to take down a team of mercenaries: Dominic unexpectedly gets sidetracked with facing his presumed deceased girlfriend, Letty.",
        "Justin Lin",
        "Vin Diesel, Paul Walker, Dwayne Johnson, Jordana Brewster",
        "Action, Adventure, Thriller",
    )
    private val secondMovieDetail = MovieDetailItem(
        "Fast Five",
        "2011",
        "tt1596343",
        "https://m.media-amazon.com/images/M/MV5BMTUxNTk5MTE0OF5BMl5BanBnXkFtZTcwMjA2NzY3NA@@._V1_SX300.jpg",
        "29 Apr 2013",
        "130 min",
        "Hobbs has Dominic and Brian reassemble their crew to take down a team of mercenaries: Dominic unexpectedly gets sidetracked with facing his presumed deceased girlfriend, Letty.",
        "Justin Lin",
        "Vin Diesel, Paul Walker, Dwayne Johnson, Jordana Brewster",
        "Action, Adventure, Thriller",
    )

    @Before
    fun initTest() {
        MockitoAnnotations.initMocks(this)
        movieUseCase = SearchMovieUseCase(nwConnectivityInfo, apiRepository, bookMarkRepository)
    }

    @Test
    fun validateNoInternetCase() {
        Mockito.`when`(nwConnectivityInfo.isConnectedToInternet()).thenReturn(false)
        runBlocking {
            val resultMovies = movieUseCase.fetchMovies("Speed")
            assert(resultMovies == MovieSearchResult.NoInternet)
        }
    }

    @Test
    fun validateExceptionCase() {
        Mockito.`when`(nwConnectivityInfo.isConnectedToInternet()).thenReturn(true)
        val exception = RuntimeException("Exception Movies")
        val error = MovieSearchResult.Error(exception)

        runBlocking {
            Mockito.`when`(apiRepository.getMovies(Mockito.anyString())).thenThrow(exception)
            val searchResult = movieUseCase.fetchMovies("Speed")
            assert(searchResult == error)
        }
    }

    @Test
    fun validateNoResultCase() {
        Mockito.`when`(nwConnectivityInfo.isConnectedToInternet()).thenReturn(true)
        val movieSearchResponse = MovieSearchResponse(moviesResult = null,
            totalResults = null,
            errorText = "NoResult",
            isOK = "False")

        val noResult = MovieSearchResult.NoResult("NoResult")
        runBlocking {
            Mockito.`when`(apiRepository.getMovies(Mockito.anyString()))
                .thenReturn(movieSearchResponse)
            val result = movieUseCase.fetchMovies("Speed")
            assert(noResult == result)
        }
    }

    @Test
    fun validateSearchResultEmptyCase() {
        Mockito.`when`(nwConnectivityInfo.isConnectedToInternet()).thenReturn(true)
        val emptyResponse = MovieSearchResponse(emptyList(), null, null, "True")
        val emptyResult = MovieSearchResult.Success(emptyList())
        runBlocking {
            Mockito.`when`(apiRepository.getMovies(Mockito.anyString())).thenReturn(emptyResponse)
            val result = movieUseCase.fetchMovies("Speed")
            assert(emptyResult == result)
        }
    }

    @Test
    fun validateSearchSuccessCase() {
        Mockito.`when`(nwConnectivityInfo.isConnectedToInternet()).thenReturn(true)

        val searchItems = listOf(firstMovie, secondMovie)
        val searchResponse = MovieSearchResponse(searchItems, "632", null, "True")
        runBlocking {
            Mockito.`when`(apiRepository.getMovies(Mockito.anyString())).thenReturn(searchResponse)
            Mockito.`when`(apiRepository.getMovieDetail("tt1905041")).thenReturn(firstMovieDetail)
            Mockito.`when`(apiRepository.getMovieDetail("tt1596343")).thenReturn(secondMovieDetail)
            Mockito.`when`(bookMarkRepository.isBookMarked(Mockito.anyString())).thenReturn(false)

            val searchResult = movieUseCase.fetchMovies("Fast")
            assert(searchResult is MovieSearchResult.Success)
            val successResult = searchResult as MovieSearchResult.Success
            assert(successResult.movieList.size == 2)

            assert(successResult.movieList[0].movieId == "tt1905041")
            assert(successResult.movieList[0].title == "Fast & Furious 6")
            assert(successResult.movieList[1].movieId == "tt1596343")
            assert(successResult.movieList[1].title == "Fast Five")
        }
    }


    @Test
    fun validateMoviesBookMarkCase() {
        Mockito.`when`(nwConnectivityInfo.isConnectedToInternet()).thenReturn(true)

        val searchItems = listOf(firstMovie, secondMovie)
        val searchResponse = MovieSearchResponse(searchItems, "632", null, "True")
        runBlocking {
            Mockito.`when`(apiRepository.getMovies(Mockito.anyString())).thenReturn(searchResponse)
            Mockito.`when`(apiRepository.getMovieDetail("tt1905041")).thenReturn(firstMovieDetail)
            Mockito.`when`(apiRepository.getMovieDetail("tt1596343")).thenReturn(secondMovieDetail)

            Mockito.`when`(bookMarkRepository.isBookMarked("tt1905041")).thenReturn(true)
            Mockito.`when`(bookMarkRepository.isBookMarked("tt1596343")).thenReturn(false)


            val searchResult = movieUseCase.fetchMovies("Fast")
            assert(searchResult is MovieSearchResult.Success)
            val successResult = searchResult as MovieSearchResult.Success
            assert(successResult.movieList.size == 2)

            assert(successResult.movieList[0].movieId == "tt1905041")
            Assert.assertTrue(successResult.movieList[0].isBookMarked)
            assert(successResult.movieList[1].movieId == "tt1596343")
            Assert.assertFalse(successResult.movieList[1].isBookMarked)
        }
    }
}