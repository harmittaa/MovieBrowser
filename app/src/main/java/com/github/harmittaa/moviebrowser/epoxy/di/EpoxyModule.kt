package com.github.harmittaa.moviebrowser.epoxy.di

import com.github.harmittaa.moviebrowser.epoxy.GenresController
import com.github.harmittaa.moviebrowser.epoxy.MoviesController
import org.koin.dsl.module

val epoxyModule = module {
    factory { GenresController() }
    factory { MoviesController() }
}
