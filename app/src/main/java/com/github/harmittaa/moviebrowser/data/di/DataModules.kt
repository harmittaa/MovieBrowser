package com.github.harmittaa.moviebrowser.data.di

import com.github.harmittaa.moviebrowser.data.uc.GenreUseCase
import com.github.harmittaa.moviebrowser.data.uc.MovieUseCase
import com.github.harmittaa.moviebrowser.data.uc.Repository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
    factory { GenreUseCase(repository = get(named("genreRepo"))) }
    factory { MovieUseCase(repository = get(named("movieRepo"))) }
}

val storeRepositoryModule = module {
    single(named("genreRepo")) { Repository.provideGenreRepository(api = get()) }
    single(named("movieRepo")) { Repository.provideMovieRepository(api = get()) }
}
