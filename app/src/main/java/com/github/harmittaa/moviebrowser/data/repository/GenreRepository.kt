package com.github.harmittaa.moviebrowser.data.repository

import com.github.harmittaa.moviebrowser.data.db.MovieDatabase
import com.github.harmittaa.moviebrowser.domain.Genre
import kotlinx.coroutines.flow.Flow

class GenreRepository(val database: MovieDatabase) {
    fun getGenres(): Flow<List<Genre>> {
        return database.genreDao().getGenres()
    }
}
