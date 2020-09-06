package com.github.harmittaa.moviebrowser.domain

import androidx.room.Entity
import com.squareup.moshi.JsonClass

abstract class Genre {
    abstract val id: Int
    abstract val name: String
    abstract var movies: List<Movie>?
}

@JsonClass(generateAdapter = true)
data class GenreDto(
    override val id: Int,
    override val name: String,
    override var movies: List<Movie>?
) : Genre() {
    fun toLocal() = GenreLocal(
        id = id,
        name = name,
        movies = null
    )
}

@Entity(tableName = "genre")
data class GenreLocal(
    override val id: Int,
    override val name: String,
    override var movies: List<Movie>?
) : Genre()
