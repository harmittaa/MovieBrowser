package com.github.harmittaa.moviebrowser.domain

data class MovieGenre(
    val id: Int,
    val name: String,
    val items: List<Movie>
)
