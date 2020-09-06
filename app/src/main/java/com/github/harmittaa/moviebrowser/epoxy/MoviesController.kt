package com.github.harmittaa.moviebrowser.epoxy

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.carousel
import com.airbnb.epoxy.stickyheader.StickyHeaderCallbacks
import com.github.harmittaa.moviebrowser.MovieBindingModel_
import com.github.harmittaa.moviebrowser.browse.MovieClickListener
import com.github.harmittaa.moviebrowser.domain.GenreLocal

class MoviesController(private val clickListener: MovieClickListener) : AsyncEpoxyController(), StickyHeaderCallbacks {

    override fun isStickyHeader(position: Int): Boolean {
        return adapter.getModelAtPosition(position) is MovieBindingModel_ && position == 0
    }

    var genres: List<GenreLocal> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        genres.forEach { genre ->
            val movieModels = genre.movies!!.map { movie ->
                MovieBindingModel_().run {
                    id(movie.id)
                    movie(movie)
                    clickListener(clickListener)
                }
            }

            carousel {
                id("movieCarousel${genre.id}")
                models(movieModels)
            }
        }
    }
}
