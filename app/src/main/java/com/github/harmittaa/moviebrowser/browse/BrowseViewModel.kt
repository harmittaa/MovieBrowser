package com.github.harmittaa.moviebrowser.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.dropbox.android.external.store4.fresh
import com.github.harmittaa.moviebrowser.data.MovieRepository
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.domain.MovieGenreLocal
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.dsl.module
import timber.log.Timber

@ExperimentalCoroutinesApi
val viewModelModule = module {
    factory { BrowseViewModel(movieRepo = get()) }
}

interface MovieClickListener {
    fun onMovieClicked(movie: Movie)
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class BrowseViewModel(val movieRepo: MovieRepository) : ViewModel(), MovieClickListener {

    private val _genres: MutableLiveData<Resource<List<MovieGenreLocal>>> = MutableLiveData()
    val genres: LiveData<Resource<List<MovieGenreLocal>>> = _genres

    private val _selectedMovie: MutableLiveData<Movie> = MutableLiveData()
    val selectedMovie: LiveData<Movie> = _selectedMovie

    init {
        viewModelScope.launch {
            _genres.value = try {
                Resource.Success(movieRepo.genreStore.fresh(""))
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }

    val moviesOfCategory = genres.switchMap { genres ->
        if (genres is Resource.Loading || genres is Resource.Error) {
            liveData { emptyList<Movie>() }
        } else {
            liveData {
                genres.data?.forEach { genre ->
                    val items = movieRepo.getMoviesForCategory(genre.id).fresh(genre.id)
                    genre.items = items
                    Timber.d("Genre ${genre.name} has ${genre.items} first being ${genre.items?.first() ?: "DOESNT EXIST"}")
                }
                emit(genres)
            }
        }
    }

    override fun onMovieClicked(movie: Movie) {
        _selectedMovie.value = movie
    }
}
