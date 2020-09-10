package com.github.harmittaa.moviebrowser.browse.controllers

import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.carousel
import com.github.harmittaa.moviebrowser.GenreBrowserBindingModel_
import com.github.harmittaa.moviebrowser.browse.GenreClickListener
import com.github.harmittaa.moviebrowser.domain.Genre

class GenresController : AsyncEpoxyController() {
    lateinit var clickListener: GenreClickListener

    var selectedGenres = mutableSetOf<Genre>()
        set(value) {
            field = value
            requestModelBuild()
        }

    var genres: List<Genre> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        Carousel.setDefaultGlobalSnapHelperFactory(null)

        val genreModels = genres.map {
            GenreBrowserBindingModel_().run {
                id(it.genreId)
                genre(it)
                clickListener(clickListener)
                isSelected(selectedGenres.contains(it))

                this.clickListener(object : GenreClickListener {
                    override fun onGenreClicked(genre: Genre) {
                        this@GenresController.clickListener.onGenreClicked(genre)
                    }
                })
            }
        }

        carousel {
            id("genres_top_carousel")
            models(genreModels)
        }
    }
}
