package com.github.harmittaa.moviebrowser.data.uc

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.get
import com.github.harmittaa.moviebrowser.data.MovieApi
import com.github.harmittaa.moviebrowser.domain.GenreLocal
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.network.Resource
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalTime::class,
    ExperimentalStdlibApi::class
)
object Repository {
    fun provideGenreRepository(api: MovieApi) =
        StoreBuilder.from(Fetcher.of { api.getGenres().genres.map { it.toLocal() } }).build()

    fun provideMovieRepository(api: MovieApi) =
        StoreBuilder.from<Int, List<Movie>>(Fetcher.of { genreId ->
            api.getTopMovies(
                genreId
            ).results
        })
            .build()
}

class GenreUseCase(val repository: Store<Any, List<GenreLocal>>) {

    suspend fun getGenres() =
        try {
            Resource.Success(repository.get(Unit))
        } catch (e: Exception) {
            Resource.Error(e)
        }
}
