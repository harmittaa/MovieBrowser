package com.github.harmittaa.moviebrowser.data.uc

class GenreUseCase(private val repository: GenreRepository) {
    fun getGenres() = repository.getGenres()
}
