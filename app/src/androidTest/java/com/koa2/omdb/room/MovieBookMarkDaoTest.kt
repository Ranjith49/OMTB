package com.koa2.omdb.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieBookMarkDaoTest {

    lateinit var dataBase: MovieDataBase
    lateinit var dao: MovieBookMarkDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBase = Room.inMemoryDatabaseBuilder(context, MovieDataBase::class.java).build()
        dao = dataBase.moviesDao()
    }

    @Test
    fun validateBookMarkInsert() {
        val bookMarkData = MovieBookMarkData(movieId = "1",
            movieTitle = "ABC",
            moviePoster = "http://abc/hello.jpg")
        runBlocking {
            dao.insertMovie(bookMarkData)
            val movies = dao.getAllMoviesInline()

            assert(movies.size == 1)
            assert(movies[0].movieId == bookMarkData.movieId)
            assert(movies[0].movieTitle == bookMarkData.movieTitle)
            assert(movies[0].moviePoster == bookMarkData.moviePoster)
        }
    }

    @Test
    fun validateBookMarkDelete() {
        val bookMarkData = MovieBookMarkData(movieId = "1",
            movieTitle = "ABC",
            moviePoster = "http://abc/hello.jpg")
        runBlocking {
            dao.insertMovie(bookMarkData)
            val movies = dao.getAllMoviesInline()
            assert(movies.size == 1)
            assert(movies[0].movieId == bookMarkData.movieId)

            // Delete ..
            dao.deleteMovie(bookMarkData.movieId)
            val newList = dao.getAllMoviesInline()
            Assert.assertTrue(newList.isEmpty())
        }
    }

    @Test
    fun verifyBookMarkStatusBasedOnMovieId() {
        val bookMarkData = MovieBookMarkData(movieId = "1",
            movieTitle = "ABC",
            moviePoster = "http://abc/hello.jpg")
        runBlocking {
            val movieId = dao.isBookMarked(bookMarkData.movieId)
            Assert.assertNull(movieId)

            dao.insertMovie(bookMarkData)

            val movieIdAgain = dao.isBookMarked(bookMarkData.movieId)
            Assert.assertEquals(movieIdAgain, bookMarkData.movieId)
        }
    }

    @Test
    fun verifyFetchAllBookMarks() {
        val bookMarkData1 = MovieBookMarkData(uid = 1, movieId = "1",
            movieTitle = "ABC",
            moviePoster = "http://abc/hello.jpg")
        val bookMarkData2 = MovieBookMarkData(uid = 2, movieId = "2",
            movieTitle = "DEF",
            moviePoster = "http://def/hello.jpg")

        runBlocking {
            dao.insertMovie(bookMarkData1)
            dao.insertMovie(bookMarkData2)

            val movies = dao.getAllMovies().take(1)
            movies.collect {
                Assert.assertEquals(it.size, 2)

                Assert.assertEquals(it[0].movieId, bookMarkData2.movieId)
                Assert.assertEquals(it[0].movieTitle, bookMarkData2.movieTitle)

                Assert.assertEquals(it[1].movieId, bookMarkData1.movieId)
                Assert.assertEquals(it[1].movieTitle, bookMarkData1.movieTitle)
            }
        }
    }

    @After
    fun close() {
        dataBase.close()
    }
}