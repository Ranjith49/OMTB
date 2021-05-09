package com.koa2.omdb.usecase

import com.koa2.omdb.repository.MovieBookMarkRepository
import com.koa2.omdb.room.MovieBookMarkData
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*


class BookMarkMovieUseCaseTest {

    @Mock
    lateinit var bookMarkRepository: MovieBookMarkRepository

    private lateinit var usecase: BookMarkMovieUseCase

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        usecase = BookMarkMovieUseCase(bookMarkRepository)
    }

    @Test
    fun toggleBookMarkWhenAlreadyBookMarked() {
        val movieId = "id1"
        val bookMarkData = MovieBookMarkData(movieId = movieId,
            movieTitle = "ABC",
            moviePoster = "http://abc.com")

        runBlocking {
            `when`(bookMarkRepository.isBookMarked(movieId)).thenReturn(true)
            usecase.toggleBookMark(bookMarkData)
            verify(bookMarkRepository).deleteMovie(bookMarkData.movieId)
            verify(bookMarkRepository, never()).insertMovie(bookMarkData)
        }
    }

    @Test
    fun toggleBookMarkWhenNotBookMarked() {
        val bookMarkData = MovieBookMarkData(movieId = "123",
            movieTitle = "Movie1",
            moviePoster = "http://abc.com")
        runBlocking {
            `when`(bookMarkRepository.isBookMarked(bookMarkData.movieId)).thenReturn(false)
            usecase.toggleBookMark(bookMarkData)
            verify(bookMarkRepository).insertMovie(bookMarkData)
            verify(bookMarkRepository, never()).deleteMovie(bookMarkData.movieId)
        }
    }

    @Test
    fun validateBookMarksFetch() {
        usecase.getAllBookMarks()
        verify(bookMarkRepository).getAllBookMarks()
    }
}