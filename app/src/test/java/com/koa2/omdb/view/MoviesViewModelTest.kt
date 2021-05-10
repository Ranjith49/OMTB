package com.koa2.omdb.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koa2.omdb.view.data.MovieViewData
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*

class MoviesViewModelTest {

    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var movieItemObserver: Observer<MovieViewData>

    @Captor
    lateinit var movieItemCaptor: ArgumentCaptor<MovieViewData>

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun validateMovieClickInvokeLiveData() {
        val item = MovieViewData("123",
            "ABC",
            "http://abc/t.jpg",
            "12 April 2005",
            "123 min",
            "Hello",
            "Action",
            "ABCActor",
            "DEF",
            false)
        viewModel = MoviesViewModel()
        viewModel.movieClickLiveData.observeForever(movieItemObserver)

        viewModel.onMovieClick(item)
        Mockito.verify(movieItemObserver).onChanged(movieItemCaptor.capture())
        Assert.assertEquals(item, movieItemCaptor.value)
    }
}