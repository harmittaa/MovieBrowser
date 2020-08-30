package com.github.harmittaa.moviebrowser.data

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.StoreBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
val repositoryModule = module {
    single { MovieRepository(remote = get()) }
}

@ExperimentalCoroutinesApi
@FlowPreview
class MovieRepository(
    val remote: MovieApi
) {
    val genreStore =
        StoreBuilder.from(Fetcher.of { remote.getGenres().genres.map { it.toLocal() } }).build()

    fun getMoviesForCategory(id: Int) =
        StoreBuilder.from(Fetcher.of { remote.getMoviesForGenre(id).results })
            .build()
}
