package com.github.harmittaa.moviebrowser.domain

import androidx.room.Entity
import com.squareup.moshi.JsonClass

abstract class MovieGenre {
    abstract val id: Int
    abstract val name: String
}

@JsonClass(generateAdapter = true)
data class MovieGenreDto(
    val id: Int,
    val name: String
) {
    fun toLocal() = MovieGenreLocal(
        id = id,
        name = name,
        movies = null
    )
}

@Entity(tableName = "genre")
data class MovieGenreLocal(
    override val id: Int,
    override val name: String,
    var movies: List<Movie>?
) : MovieGenre()
