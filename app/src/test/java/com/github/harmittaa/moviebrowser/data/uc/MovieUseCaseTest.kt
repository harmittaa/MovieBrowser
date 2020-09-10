package com.github.harmittaa.moviebrowser.data.uc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dropbox.android.external.store4.Store
import com.github.harmittaa.moviebrowser.domain.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@RunWith(JUnit4::class)
class MovieUseCaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var repository: Store<Int, List<Movie>>
    private lateinit var useCase: MovieUseCase

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setUp() {
/*
        Dispatchers.setMain(mainThreadSurrogate)
        repository = mock()
        useCase = MovieUseCase(repository = repository)
*/
    }

    @Test
    fun `test getMovies when repository throws an error then Error resource is returned`() = runBlockingTest {
/*
        val genreId = 123
        val errorMessage = "Error"

        `when`(repository.fresh(anyInt())).thenThrow(Exception(errorMessage))
        //whenever(repository.fresh(genreId)).thenThrow(Exception(errorMessage))

        val genre = MovieGenreLocal(genreId, "", null)
        val response = useCase.getMovies(listOf(genre)).first()

        verify(repository.fresh(genreId))
        assertTrue(response is Resource.Error)
        assertEquals(errorMessage, (response as Resource.Error).message)
        assertTrue(true)
*/
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}
