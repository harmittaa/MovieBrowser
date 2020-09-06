package com.github.harmittaa.moviebrowser.data.uc

import com.dropbox.android.external.store4.*
import com.github.harmittaa.moviebrowser.data.MovieApi
import com.github.harmittaa.moviebrowser.db.MovieDatabase
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.GenreLocal
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.domain.MovieLocal
import com.github.harmittaa.moviebrowser.network.Resource
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalTime::class,
    ExperimentalStdlibApi::class
)
object Repository {
    fun provideGenreRepository(api: MovieApi, db: MovieDatabase) = StoreBuilder.from(
        Fetcher.of { api.getGenres().genres.map { it.toLocal() } }
    ).build()

    fun provideMovieRepository(api: MovieApi, db: MovieDatabase): Store<Genre, List<Movie>> {
        return StoreBuilder
            .from(
                Fetcher.of { genre: Genre ->
                    api.getMoviesForGenre(genre.genreId).results.map { it.toLocal() }
                },
                sourceOfTruth = SourceOfTruth.of(
                    reader = db.genreDao()::getGenresMovies,
                    writer = db.genreDao()::insertGenresMovies,
                    delete = db.genreDao()::deleteGenre,
                    deleteAll = db.genreDao()::deleteAll
                )
            )
            .build()
    }

}

class GenreUseCase(val repository: Store<Unit, List<Genre>>) {

    suspend fun getGenres() =
        try {
            Resource.Success(repository.get(Unit))
        } catch (e: Exception) {
            Timber.e("EXC: $e")
            Resource.Error(e)
        }
}
