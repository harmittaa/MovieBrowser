package com.github.harmittaa.moviebrowser.data

import com.github.harmittaa.moviebrowser.domain.GenreDto
import com.github.harmittaa.moviebrowser.domain.MovieDto
import retrofit2.http.GET
import retrofit2.http.Query

data class GenresResponse(val genres: List<GenreDto>)
data class DiscoverEnvelope(
    val results: List<MovieDto>
)

interface MovieApi {

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse

    @GET("movie/top_rated")
    suspend fun getMoviesForGenre(
        @Query(encoded = true, value = "with_genres") genre: String,
        @Query("vote_count.gte") count: Int = 1000,
        @Query("sort_by") arg: String = "vote_average.desc"
    ): DiscoverEnvelope
}
