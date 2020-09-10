package com.github.harmittaa.moviebrowser.data.di

import com.github.harmittaa.moviebrowser.data.uc.GenreRepository
import com.github.harmittaa.moviebrowser.data.uc.GenreUseCase
import com.github.harmittaa.moviebrowser.data.uc.MovieUseCase
import com.github.harmittaa.moviebrowser.data.uc.Repository
import com.github.harmittaa.moviebrowser.db.di.databaseModule
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
    factory { GenreUseCase(repository = get()) }
    factory { MovieUseCase(repository = get(named("movieRepo"))) }
    single { GenreRepository(database = get()) }
}

val storeRepositoryModule = module {
    loadKoinModules(databaseModule)
    single(named("movieRepo")) { Repository.provideMovieRepository(api = get(), db = get()) }
}
