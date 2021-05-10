package com.koa2.omdb.view.bookmark

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.koa2.omdb.room.MovieBookMarkData
import com.koa2.omdb.usecase.BookMarkMovieUseCase
import com.koa2.omdb.view.TestCoroutineRule
import com.koa2.omdb.view.bookmark.BookMarkViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

@ExperimentalCoroutinesApi
class BookMarkViewModelTest {

    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var bookMarkMovieUseCase: BookMarkMovieUseCase

    @Mock
    lateinit var bookMarkObserver: Observer<List<MovieBookMarkData>>

    @Captor
    lateinit var bookMarkCaptor: ArgumentCaptor<List<MovieBookMarkData>>

    private lateinit var viewModel: BookMarkViewModel

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testBookMarkFetch() {
        val bookMarkData1 = MovieBookMarkData(uid = 1, movieId = "1",
            movieTitle = "ABC",
            moviePoster = "http://abc/hello.jpg")
        val bookMarkData2 = MovieBookMarkData(uid = 2, movieId = "2",
            movieTitle = "DEF",
            moviePoster = "http://def/hello.jpg")
        testCoroutineRule.runBlockingTest {
            val items = flow {
                emit(listOf(bookMarkData1, bookMarkData2))
            }.take(1)

            Mockito.`when`(bookMarkMovieUseCase.getAllBookMarks()).thenReturn(items)

            viewModel = BookMarkViewModel(bookMarkMovieUseCase)
            viewModel.bookMarksData.observeForever(bookMarkObserver)

            Mockito.verify(bookMarkObserver).onChanged(bookMarkCaptor.capture())
            assert(bookMarkCaptor.value == listOf(bookMarkData1, bookMarkData2))
        }
    }
}