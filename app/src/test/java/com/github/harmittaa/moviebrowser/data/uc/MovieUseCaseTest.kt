package com.github.harmittaa.moviebrowser.data.uc

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.Movie
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalTime::class,
    ExperimentalStdlibApi::class
)
@RunWith(JUnit4::class)
class MovieUseCaseTest {
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var repository: Store<List<Genre>, List<Movie>>
    private lateinit var useCase: MovieUseCase
    private val movieList: List<Movie> = listOf(mock())

    @Before
    fun setUp() {
        repository = mock()
        useCase = MovieUseCase(repository = repository)
    }

    @Ignore("Failing coroutine mocks")
    @Test
    private fun `test getMovies when repository throws an error then Error resource is returned`() =
        runBlockingTest {
            val fetcher: FakeFetcher<List<Genre>, List<Movie>> = mock()
            val persister: FakePersister<List<Genre>, List<Movie>, List<Movie>> = mock()
            val repository = StoreBuilder.from(fetcher, sourceOfTruth = persister).build()

            whenever(fetcher.getData(any())).thenReturn(movieList)
            whenever(persister.get(any())).thenReturn(movieList)

            useCase = MovieUseCase(repository, testDispatcher)

            val result = useCase.getMovies(listOf()).toList()

            assertSame(result.first().data!!, movieList)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    abstract class FakeFetcher<Key : Any, Output : Any> : Fetcher<Key, Output> {
        abstract fun getData(f: (Key) -> Output): Output
    }

    abstract class FakePersister<Key : Any, Input : Any, Output : Any> :
        SourceOfTruth<Key, Input, Output> {
        abstract fun get(f: (Key) -> Output): Output
        abstract fun insert(f: (Key, Output) -> Unit)
        abstract fun delete(f: (Key) -> Unit)
        abstract fun deleteAl(f: () -> Unit)
    }
}
