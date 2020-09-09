package com.github.harmittaa.moviebrowser.epoxy

import android.view.View
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.carousel
import com.github.harmittaa.moviebrowser.GenreBrowserBindingModel_
import com.github.harmittaa.moviebrowser.browse.MovieClickListener
import com.github.harmittaa.moviebrowser.domain.Genre

class GenresController(private val clickListener: MovieClickListener) : AsyncEpoxyController() {
    private val selectedGenres = mutableSetOf<Genre>()

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

                    this.clickListener(object : MovieClickListener {
                        override fun onGenreClicked(view: View, genre: Genre) {
                            val addSucceeded = selectedGenres.add(genre)
                            view.isSelected = addSucceeded
                            if (!addSucceeded) {
                                selectedGenres.remove(genre)
                            }
                            this@GenresController.clickListener.onGenreClicked(view, genre)
                            requestModelBuild()
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
