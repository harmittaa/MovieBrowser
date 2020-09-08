package com.github.harmittaa.moviebrowser.epoxy

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.carousel
import com.github.harmittaa.moviebrowser.MovieCardVerticalBindingModel_
import com.github.harmittaa.moviebrowser.browse.MovieClickListener
import com.github.harmittaa.moviebrowser.domain.GenreLocal
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.movieCardHorizontal

private const val TOP_ITEM_COUNT = 5
class MoviesController(private val clickListener: MovieClickListener) : AsyncEpoxyController() {

    var genres: List<GenreLocal> = emptyList()
        set(value) {
            field = value
            movies = value.first().movies ?: emptyList()
            requestModelBuild()
        }

    var movies: List<Movie> = emptyList()

    override fun buildModels() {
        val topMovieModels = movies.take(TOP_ITEM_COUNT).map { movie ->
            MovieCardVerticalBindingModel_().run {
                id("top_movies_${movie.id}")
                movie(movie)
                clickListener(clickListener)
            }
        }

        carousel {
            id("top_movies")
            models(topMovieModels)
            padding(Carousel.Padding.dp(16, 4, 16, 16, 16))
        }

        movies.takeLast(movies.count() - TOP_ITEM_COUNT).forEach { movie ->
            movieCardHorizontal {
                id(movie.id)
                movie(movie)
                clickListener(clickListener)
            }
        }
    }
}
