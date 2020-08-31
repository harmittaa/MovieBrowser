package com.github.harmittaa.moviebrowser.data.uc

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.get
import com.github.harmittaa.moviebrowser.data.MovieApi
import com.github.harmittaa.moviebrowser.data.uc.Repository.provideGenreRepository
import com.github.harmittaa.moviebrowser.data.uc.Repository.provideMovieRepository
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.domain.MovieGenreLocal
import com.github.harmittaa.moviebrowser.network.Resource
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.core.qualifier.named
import org.koin.dsl.module

val storeRepositoryModule = module {
    single(named("genreRepo")) { provideGenreRepository(api = get()) }
    single(named("movieRepo")) { provideMovieRepository(api = get()) }
}

val useCaseModule = module {
    factory { GenreUseCase(repository = get(named("genreRepo"))) }
    factory { MovieUseCase(repository = get(named("movieRepo"))) }
}

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
            api.getMoviesForGenre(
                genreId
            ).results
        })
            .build()
}

class GenreUseCase(val repository: Store<Any, List<MovieGenreLocal>>) {

    suspend fun getGenres() =
        try {
            Resource.Success(repository.get(Unit))
        } catch (e: Exception) {
            Resource.Error(e)
        }
}
