package com.github.harmittaa.moviebrowser.data.uc

import com.dropbox.android.external.store4.get

class GenreUseCase(val repository: GenreRepository) {

    fun getGenres() = repository.getGenres()
}
