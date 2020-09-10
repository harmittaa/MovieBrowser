package com.github.harmittaa.moviebrowser.browse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.harmittaa.moviebrowser.data.uc.GenreUseCase
import com.github.harmittaa.moviebrowser.data.uc.MovieUseCase
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.GenreLocal
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.network.Resource
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@RunWith(JUnit4::class)
class BrowseViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val defaultTimeout = 5_00L
    private val errorString = "Error"
    private val movie: Movie = mock()
    private val genre: Genre = GenreLocal(123, "genre name")
    private val genres = listOf(genre)
    private val movies = listOf(movie)
    private val movieResource = Resource.Success(movies)
    private val errorResource = Resource.Error(errorString)
    private val loadingResource = Resource.Loading
    private var genreUseCase: GenreUseCase = mock()
    private var movieUseCase: MovieUseCase = mock()
    private lateinit var viewModel: BrowseViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        whenever(genreUseCase.getGenres()).thenReturn(flowOf(genres))
        viewModel = BrowseViewModel(genreUseCase, movieUseCase)
    }

    @Test
    fun `test clearFilters - when filters are cleared then selected genres is empty`() {
        // given
        val observer: Observer<MutableSet<Genre>> = mock()
        viewModel.selectedGenres.observeForever(observer)

        // when
        viewModel.clearFilters()

        // then
        verify(observer, timeout(defaultTimeout)).onChanged(mutableSetOf())
    }

    @Test
    fun `test onGenreClicked - when genre is not yet selected, then it is added to list`() {
        // given
        val observer: Observer<MutableSet<Genre>> = mock()
        viewModel.selectedGenres.observeForever(observer)

        // when
        viewModel.onGenreClicked(genre)

        // then
        verify(observer, timeout(defaultTimeout)).onChanged(mutableSetOf(genre))
    }

    @Test
    fun `test onGenreClicked - when genre is already selected, then it is removed from list`() {
        // given
        val observer: Observer<MutableSet<Genre>> = mock()
        viewModel.selectedGenres.observeForever(observer)

        // when
        viewModel.onGenreClicked(genre)
        verify(observer).onChanged(mutableSetOf(genre))
        viewModel.onGenreClicked(genre)

        // then
        verify(observer, timeout(defaultTimeout).times(2)).onChanged(mutableSetOf())
    }

    @Test
    fun `test moviesOfCategory - when usecase emits loading is selected then loading is emitted`() {
        // given
        whenever(movieUseCase.getMovies(any())).thenReturn(flowOf(loadingResource))
        val observer: Observer<Resource<List<Movie>>> = mock()

        // when
        viewModel.moviesOfCategory.observeForever(observer)
        verify(observer, timeout(defaultTimeout)).onChanged(loadingResource)
        viewModel.onGenreClicked(genre)

        // then
        verify(observer, timeout(defaultTimeout).times(2)).onChanged(loadingResource)
    }

    @Test
    fun `test moviesOfCategory - when UseCase emits data is selected then data is emitted`() {
        // given
        whenever(movieUseCase.getMovies(any())).thenReturn(flowOf(movieResource))
        val observer: Observer<Resource<List<Movie>>> = mock()

        // when
        viewModel.moviesOfCategory.observeForever(observer)
        verify(observer, timeout(defaultTimeout)).onChanged(movieResource)
        viewModel.onGenreClicked(genre)

        // then
        verify(observer, timeout(defaultTimeout).times(2)).onChanged(movieResource)
    }

    @Test
    fun `test moviesOfCategory - when UseCase emits error is selected then error is emitted`() {
        // given
        whenever(movieUseCase.getMovies(any())).thenReturn(flowOf(errorResource))
        val observer: Observer<Resource<List<Movie>>> = mock()

        // when
        viewModel.moviesOfCategory.observeForever(observer)
        verify(observer, timeout(defaultTimeout)).onChanged(errorResource)
        viewModel.onGenreClicked(genre)

        // then
        verify(observer, timeout(defaultTimeout).times(2)).onChanged(errorResource)
    }

    @Test
    fun `test moviesOfCategory - when UseCase emits all states is selected then states are emitted`() {
        // given
        whenever(movieUseCase.getMovies(any())).thenReturn(flowOf(loadingResource))
        val observer: Observer<Resource<List<Movie>>> = mock()

        // when & then
        // loading
        viewModel.moviesOfCategory.observeForever(observer)
        verify(observer, timeout(defaultTimeout)).onChanged(loadingResource)
        viewModel.onGenreClicked(genre)
        verify(observer, timeout(defaultTimeout).times(2)).onChanged(loadingResource)

        // success
        whenever(movieUseCase.getMovies(any())).thenReturn(flowOf(movieResource))
        viewModel.onGenreClicked(genre)
        verify(observer, timeout(defaultTimeout)).onChanged(movieResource)

        // error
        whenever(movieUseCase.getMovies(any())).thenReturn(flowOf(errorResource))
        viewModel.onGenreClicked(genre)
        verify(observer, timeout(defaultTimeout)).onChanged(errorResource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}
