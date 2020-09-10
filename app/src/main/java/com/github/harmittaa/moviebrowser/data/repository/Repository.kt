package com.github.harmittaa.moviebrowser.data.repository

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.github.harmittaa.moviebrowser.data.MovieApi
import com.github.harmittaa.moviebrowser.data.db.MovieDatabase
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.Movie
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
    fun provideMovieRepository(api: MovieApi, db: MovieDatabase): Store<List<Genre>, List<Movie>> {
        return StoreBuilder.from(
            fetcher = Fetcher.of {
                    genres: List<Genre> ->
                val genresList = genres.map { it.genreId }.joinToString(",")
                api.getMoviesForGenre(genresList).results
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = db.genreDao()::getMoviesOfGenres,
                writer = db.genreDao()::insertGenresMovies,
                delete = db.genreDao()::deleteMoviesOfGenre,
                deleteAll = db.genreDao()::deleteAll
            )
        )
            .build()
    }
}
