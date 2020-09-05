package com.github.harmittaa.moviebrowser.epoxy

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.carousel
import com.github.harmittaa.moviebrowser.GenreBrowserBindingModel_
import com.github.harmittaa.moviebrowser.browse.MovieClickListener
import com.github.harmittaa.moviebrowser.domain.MovieGenre
import com.github.harmittaa.moviebrowser.domain.MovieGenreLocal

class GenresController(private val clickListener: MovieClickListener) : AsyncEpoxyController() {
    private var selected: MovieGenre? = null

    var genres: List<MovieGenreLocal> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        val genreModels = genres.map {
            GenreBrowserBindingModel_().run {
                id(it.id)
                genre(it)
            }
        }

        carousel {
            id("genres_top_carousel")
            models(genreModels)
            padding(Carousel.Padding.dp(0, 4, 0, 16, 8))
        }
    }
}
