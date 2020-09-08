package com.github.harmittaa.moviebrowser.epoxy

import android.view.View
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.carousel
import com.github.harmittaa.moviebrowser.GenreBrowserBindingModel_
import com.github.harmittaa.moviebrowser.browse.MovieClickListener
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.Movie
import timber.log.Timber

class GenresController(private val clickListener: MovieClickListener) : AsyncEpoxyController() {

    var genres: List<Genre> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        val genreModels = genres.map {
            GenreBrowserBindingModel_().run {
                id(it.genreId)
                genre(it)
                clickListener(clickListener)
                this.clickListener(object : MovieClickListener {
                    override fun onMovieClicked(view: View, movie: Movie) {
                        Timber.d("CLIIGK $view")
                    }

                    override fun onGenreClicked(view: View, genre: Genre) {
                        Timber.d("CLIIGK $view")
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
