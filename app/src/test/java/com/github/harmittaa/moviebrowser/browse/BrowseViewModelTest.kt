package com.github.harmittaa.moviebrowser.browse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.harmittaa.moviebrowser.data.uc.GenreUseCase
import com.github.harmittaa.moviebrowser.data.uc.MovieUseCase
import com.github.harmittaa.moviebrowser.domain.GenreLocal
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.network.Resource
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
import kotlinx.coroutines.test.runBlockingTest
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
    private val genre = GenreLocal(123, "", listOf(movie))
    private val genres = listOf(genre)
    private val genresResource = Resource.Success(genres)
    private val errorResource = Resource.Error(errorString)
    private var genreUseCase: GenreUseCase = mock()
    private var movieUseCase: MovieUseCase = mock()
    private lateinit var viewModel: BrowseViewModel

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel = BrowseViewModel(genreUseCase, movieUseCase)
    }

    @Test
    fun `test init, when viewModel is created then resource emits`() =
        runBlockingTest {
            whenever(genreUseCase.getGenres()).thenReturn(genresResource)
            val observer: Observer<Resource<List<GenreLocal>>> = mock()

            viewModel.genres.observeForever(observer)
            viewModel.onCreateView()

            verify(observer, timeout(defaultTimeout)).onChanged(Resource.Loading)
            verify(observer, timeout(defaultTimeout)).onChanged(genresResource)
        }

    @Test
    fun `test init, when genreUseCase throws error then error is emitted`() =
        runBlockingTest {
            whenever(genreUseCase.getGenres()).thenReturn(errorResource)
            val observer: Observer<Resource<List<GenreLocal>>> = mock()

            viewModel.genres.observeForever(observer)
            viewModel.onCreateView()

            verify(observer, timeout(defaultTimeout)).onChanged(Resource.Loading)
            verify(observer, timeout(defaultTimeout)).onChanged(errorResource)
        }

    @Test
    fun `test init, when genres are fetched then categories are invoked`() =
        runBlockingTest {

            whenever(genreUseCase.getGenres()).thenReturn(genresResource)
            whenever(movieUseCase.getMovies(genres)).thenReturn(flowOf(genresResource))
            val observer: Observer<Resource<List<GenreLocal>>> = mock()

            viewModel.moviesOfCategory.observeForever(observer)
            viewModel.onCreateView()

            verify(observer, timeout(defaultTimeout)).onChanged(genresResource)
        }

    @Test
    fun `test init, when genres throws error then movies throws error`() =
        runBlockingTest {
            whenever(genreUseCase.getGenres()).thenReturn(errorResource)
            val observer: Observer<Resource<List<GenreLocal>>> = mock()

            viewModel.moviesOfCategory.observeForever(observer)
            viewModel.onCreateView()

            verify(observer, timeout(defaultTimeout)).onChanged(errorResource)
        }

    @Test
    fun `test onMovieClicked, when invoked then selectedMovie emits`() =
        runBlockingTest {
            val observer: Observer<Movie> = mock()

            viewModel.selectedMovie.observeForever(observer)
            viewModel.onMovieClicked(movie)

            verify(observer, timeout(defaultTimeout)).onChanged(movie)
        }

    @Test
    fun `test onGenreClicked, when invoked then selectedGenre emits`() =
        runBlockingTest {
            val observer: Observer<Int> = mock()
            whenever(genreUseCase.getGenres()).thenReturn(genresResource)

            viewModel.onCreateView()
            verify(genreUseCase, timeout(defaultTimeout)).getGenres()
            viewModel.selectedGenre.observeForever(observer)
            viewModel.onGenreClicked(genre)

            verify(observer, timeout(defaultTimeout)).onChanged(0)
        }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}
