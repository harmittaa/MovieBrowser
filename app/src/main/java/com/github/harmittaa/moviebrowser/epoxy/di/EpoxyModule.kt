package com.github.harmittaa.moviebrowser.epoxy.di

import com.github.harmittaa.moviebrowser.browse.controllers.GenresController
import com.github.harmittaa.moviebrowser.browse.controllers.MoviesController
import org.koin.dsl.module

val epoxyModule = module {
    factory { GenresController() }
    factory { MoviesController() }
}
