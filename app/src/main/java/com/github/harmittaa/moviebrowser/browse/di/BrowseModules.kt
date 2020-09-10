package com.github.harmittaa.moviebrowser.browse.di

import com.github.harmittaa.moviebrowser.browse.BrowseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val viewModelModule = module {
    factory { BrowseViewModel(genreUseCase = get(), movieUseCase = get()) }
}
