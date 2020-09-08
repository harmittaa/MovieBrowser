package com.github.harmittaa.moviebrowser.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

abstract class Movie {
    abstract val movieId: Int
    abstract val title: String
    abstract val overview: String
    abstract val backdropUrl: String
    abstract val posterUrl: String
    abstract val rating: String
    abstract val genreIds: List<Int>
    fun getBackdropPath() = "https://image.tmdb.org/t/p/w780/$backdropUrl"
    fun getPosterPath() = "https://image.tmdb.org/t/p/w780/$posterUrl"
}

@JsonClass(generateAdapter = true)
data class MovieDto(
    @Json(name = "id") override val movieId: Int,
    override val title: String,
    override val overview: String,
    @Json(name = "backdrop_path") override val backdropUrl: String,
    @Json(name = "poster_path") override val posterUrl: String,
    @Json(name = "vote_average") override val rating: String,
    @Json(name = "genre_ids") override val genreIds: List<Int>
) : Movie() {
    fun toLocal() = MovieLocal(
        movieId = movieId,
        title = title,
        overview = overview,
        backdropUrl = backdropUrl,
        posterUrl = posterUrl,
        rating = rating,
        genreIds = genreIds
    )
}

@Entity(tableName = "movie")
data class MovieLocal(
    @PrimaryKey
    override val movieId: Int,
    override val title: String,
    override val overview: String,
    override val backdropUrl: String,
    override val posterUrl: String,
    override val rating: String,
    override val genreIds: List<Int>
) : Movie()
