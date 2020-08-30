package com.github.harmittaa.moviebrowser.data

import com.github.harmittaa.moviebrowser.domain.MovieGenreDto
import retrofit2.http.GET

data class GenresResponse(val genres: List<MovieGenreDto>)

interface MovieApi {

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse
}
