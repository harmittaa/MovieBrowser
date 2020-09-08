package com.github.harmittaa.moviebrowser.data

import com.github.harmittaa.moviebrowser.domain.GenreDto
import com.github.harmittaa.moviebrowser.domain.Movie
import retrofit2.http.GET
import retrofit2.http.Query

data class GenresResponse(val genres: List<GenreDto>)
data class DiscoverEnvelope(
    val results: List<Movie>
)

interface MovieApi {

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse

    @GET("discover/movie")
    suspend fun getMoviesForGenre(
        @Query("with_genres") genre: Int,
        @Query("vote_count.gte") count: Int = 1000,
        @Query("sort_by") arg: String = "vote_average.desc"
    ): DiscoverEnvelope

    @GET("discover/movie")
    suspend fun getTopMovies(
        @Query("with_genres") genre: Int,
        @Query("vote_count.gte") count: Int = 1000,
        @Query("sort_by") arg: String = "vote_average.desc"
    ): DiscoverEnvelope
}
