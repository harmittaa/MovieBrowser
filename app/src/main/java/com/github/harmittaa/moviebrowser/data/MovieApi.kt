package com.github.harmittaa.moviebrowser.data

import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.domain.MovieGenreDto
import retrofit2.http.GET
import retrofit2.http.Query

data class GenresResponse(val genres: List<MovieGenreDto>)
data class DiscoverEnvelope(
    val results: List<Movie>
)

interface MovieApi {

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse

    @GET("discover/movie")
    suspend fun getMoviesForGenre(
        @Query("with_genres") genre: Int,
        @Query("vote_count.gte") count: Int = 500
    ): DiscoverEnvelope
}
