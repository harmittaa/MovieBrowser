package com.github.harmittaa.moviebrowser.epoxy

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.carousel
import com.github.harmittaa.moviebrowser.GenreBrowserBindingModel_
import com.github.harmittaa.moviebrowser.browse.MovieClickListener
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.Movie

class GenresController(private val clickListener: MovieClickListener) : AsyncEpoxyController() {
    private var selected: Genre? = null

    var genres: List<Genre> = emptyList()
        set(value) {
            field = value
            movies = value.first().items ?: emptyList()
            requestModelBuild()
        }

    var movies: List<Movie> = emptyList()

    override fun buildModels() {
        val genreModels = genres.map {
            GenreBrowserBindingModel_().run {
                id(it.genreId)
                genre(it)
                clickListener(clickListener)
            }
        }

        carousel {
            id("genres_top_carousel")
            models(genreModels)
        }
    }
}
