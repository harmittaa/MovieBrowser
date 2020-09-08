package com.github.harmittaa.moviebrowser.data.uc

import com.github.harmittaa.moviebrowser.db.MovieDatabase
import com.github.harmittaa.moviebrowser.domain.Genre
import kotlinx.coroutines.flow.Flow

class GenreRepository(val database: MovieDatabase) {
    fun getGenres(): Flow<List<Genre>> {
        return database.genreDao().getGenres()
    }
}
