package com.github.harmittaa.moviebrowser.data.uc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.harmittaa.moviebrowser.domain.Genre
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@RunWith(JUnit4::class)
class GenreUseCaseTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var genreUseCase: GenreUseCase
    private val repository: GenreRepository = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        genreUseCase = GenreUseCase(repository)
    }

    @Test
    fun `test getGenres - when genres are called then repository response is returned`() = runBlocking {
        // given
        val genre: Genre = mock()
        whenever(repository.getGenres()).thenReturn(flowOf(listOf(genre)))

        // when
        val repoList = genreUseCase.getGenres().toList()

        // then
        assertSame(genre, repoList.first().first())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}
