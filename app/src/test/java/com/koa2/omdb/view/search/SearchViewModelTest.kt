package com.koa2.omdb.view.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.koa2.omdb.room.MovieBookMarkData
import com.koa2.omdb.usecase.BookMarkMovieUseCase
import com.koa2.omdb.usecase.SearchMovieUseCase
import com.koa2.omdb.view.TestCoroutineRule
import com.koa2.omdb.view.data.MovieSearchResult
import com.koa2.omdb.view.data.MovieViewData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var searchMovieUseCase: SearchMovieUseCase

    @Mock
    lateinit var bookMarkMovieUseCase: BookMarkMovieUseCase

    @Mock
    lateinit var searchResultObserver: Observer<List<MovieViewData>>

    @Mock
    lateinit var searchProgressObserver: Observer<Boolean>

    @Mock
    lateinit var searchErrorObserver: Observer<String>

    @Mock
    lateinit var bookMarkObserver: Observer<MovieViewData>

    @Captor
    lateinit var searchResultCaptor: ArgumentCaptor<List<MovieViewData>>

    @Captor
    lateinit var searchErrorCaptor: ArgumentCaptor<String>

    @Captor
    lateinit var searchProgressCaptor: ArgumentCaptor<Boolean>

    @Captor
    lateinit var bookMarkCaptor: ArgumentCaptor<MovieViewData>

    lateinit var viewModel: SearchViewModel

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        viewModel = SearchViewModel(searchMovieUseCase, bookMarkMovieUseCase)

        viewModel.searchInProgress.observeForever(searchProgressObserver)
        viewModel.searchMovieError.observeForever(searchErrorObserver)
        viewModel.searchMovieResult.observeForever(searchResultObserver)
        viewModel.bookMarkUpdateLiveData.observeForever(bookMarkObserver)
    }

    @Test
    fun validateSearchResultWhenTextIsEmpty() {
        viewModel.searchMovies("")
        Mockito.verify(searchErrorObserver).onChanged(searchErrorCaptor.capture())
        assert(SearchViewModel.EMPTY_SEARCH_TEXT == searchErrorCaptor.value)
    }

    @Test
    fun validateSearchResultWhenNoResult() {
        testCoroutineRule.runBlockingTest {
            val noResult = MovieSearchResult.NoResult("Too Many Results")
            Mockito.`when`(searchMovieUseCase.fetchMovies(Mockito.anyString()))
                .thenReturn(noResult)

            viewModel.searchMovies("Movies")

            Mockito.verify(searchProgressObserver, Mockito.times(2))
                .onChanged(searchProgressCaptor.capture())
            Mockito.verify(searchErrorObserver).onChanged(searchErrorCaptor.capture())

            assert(searchProgressCaptor.allValues[0] == true)
            assert(searchProgressCaptor.allValues[1] == false)
            assert(searchErrorCaptor.value == noResult.errorText)
        }
    }

    @Test
    fun validateSearchResultWhenError() {
        testCoroutineRule.runBlockingTest {
            val error = MovieSearchResult.Error(IllegalAccessException("Error"))
            Mockito.`when`(searchMovieUseCase.fetchMovies(Mockito.anyString())).thenReturn(error)

            viewModel.searchMovies("Movies")

            Mockito.verify(searchProgressObserver, Mockito.times(2))
                .onChanged(searchProgressCaptor.capture())
            Mockito.verify(searchErrorObserver).onChanged(searchErrorCaptor.capture())

            assert(searchProgressCaptor.allValues[0] == true)
            assert(searchProgressCaptor.allValues[1] == false)
            assert(searchErrorCaptor.value == error.error.localizedMessage)
        }
    }

    @Test
    fun validateNoInternetCase() {
        testCoroutineRule.runBlockingTest {
            val noInternet = MovieSearchResult.NoInternet
            Mockito.`when`(searchMovieUseCase.fetchMovies(Mockito.anyString()))
                .thenReturn(noInternet)

            viewModel.searchMovies("Movies")

            Mockito.verify(searchProgressObserver, Mockito.times(2))
                .onChanged(searchProgressCaptor.capture())
            Mockito.verify(searchErrorObserver).onChanged(searchErrorCaptor.capture())

            assert(searchProgressCaptor.allValues[0] == true)
            assert(searchProgressCaptor.allValues[1] == false)
            assert(searchErrorCaptor.value == SearchViewModel.NO_INTERNET)
        }
    }

    @Test
    fun validateSearchResultInMovies() {
        testCoroutineRule.runBlockingTest {
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

            Mockito.`when`(searchMovieUseCase.fetchMovies(Mockito.anyString()))
                .thenReturn(MovieSearchResult.Success(listOf(item)))

            viewModel.searchMovies("Movies")
            Mockito.verify(searchProgressObserver, Mockito.times(2))
                .onChanged(searchProgressCaptor.capture())
            Mockito.verify(searchResultObserver).onChanged(searchResultCaptor.capture())

            assert(searchProgressCaptor.allValues[0] == true)
            assert(searchProgressCaptor.allValues[1] == false)

            assert(searchResultCaptor.value.size == 1)
            assert(searchResultCaptor.value[0] == item)
        }
    }

    @Test
    fun validateBookMarkClickWhenSavedSuccess() {
        testCoroutineRule.runBlockingTest {
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
            viewModel.onMovieBookMarkClick(item)
            Mockito.verify(bookMarkObserver).onChanged(bookMarkCaptor.capture())
            assert(bookMarkCaptor.value == item.copy(isBookMarked = !item.isBookMarked))
        }
    }

    @Test
    fun validateBookMarkClickWhenSaveFailed() {
        testCoroutineRule.runBlockingTest {
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
            val data = MovieBookMarkData(movieId = item.movieId,movieTitle = item.title,moviePoster = item.poster)
            Mockito.`when`(bookMarkMovieUseCase.toggleBookMark(data))
                .thenThrow(RuntimeException("Error"))
            viewModel.onMovieBookMarkClick(item)
            Mockito.verify(bookMarkObserver, Mockito.never()).onChanged(bookMarkCaptor.capture())
        }
    }
}