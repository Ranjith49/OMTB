package com.koa2.omdb.repository

import com.koa2.omdb.room.MovieBookMarkDao
import com.koa2.omdb.room.MovieBookMarkData
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MovieBookMarkRepositoryTest {

    @Mock
    lateinit var bookMarkDao: MovieBookMarkDao
    lateinit var repository: MovieBookMarkRepository

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        repository = MovieBookMarkRepository(bookMarkDao)
    }

    @Test
    fun validateBookMarkStatus() {
        val movieId = "123"
        runBlocking {
            Mockito.`when`(bookMarkDao.isBookMarked(movieId)).thenReturn(null)
            val isBookMarked = repository.isBookMarked(movieId)
            Assert.assertFalse(isBookMarked)

            Mockito.`when`(bookMarkDao.isBookMarked(movieId)).thenReturn("123")
            val isBookMarked2 = repository.isBookMarked(movieId)
            Assert.assertTrue(isBookMarked2)
        }
    }

    @Test
    fun validateInsertMovie() {
        val movieData = MovieBookMarkData(movieId = "123",
            movieTitle = "ABC",
            moviePoster = "https://abc.com/hello.jpg")
        runBlocking {
            repository.insertMovie(movieData)
            Mockito.verify(bookMarkDao).insertMovie(movieData)
        }
    }

    @Test
    fun validateDeleteMovie() {
        runBlocking {
            repository.deleteMovie("123")
            Mockito.verify(bookMarkDao).deleteMovie("123")
        }
    }

    @Test
    fun validateFetchBookMarks() {
        repository.getAllBookMarks()
        Mockito.verify(bookMarkDao).getAllMovies()
    }
}