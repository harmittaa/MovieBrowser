package com.github.harmittaa.moviebrowser.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    @Json(name = "backdrop_path") val backdropUrl: String,
    @Json(name = "poster_path") val posterUrl: String
) {
    fun getBackdropPath() = "https://image.tmdb.org/t/p/w780/$backdropUrl"
    fun getPosterPath() = "https://image.tmdb.org/t/p/w780/$posterUrl"
}
