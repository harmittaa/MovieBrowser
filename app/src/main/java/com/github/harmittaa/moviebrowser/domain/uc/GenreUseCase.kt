package com.github.harmittaa.moviebrowser.domain.uc

import com.github.harmittaa.moviebrowser.data.repository.GenreRepository

class GenreUseCase(private val repository: GenreRepository) {
    fun getGenres() = repository.getGenres()
}
