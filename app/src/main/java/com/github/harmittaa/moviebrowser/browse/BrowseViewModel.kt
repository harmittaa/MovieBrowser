package com.github.harmittaa.moviebrowser.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.github.harmittaa.moviebrowser.data.uc.GenreUseCase
import com.github.harmittaa.moviebrowser.data.uc.MovieUseCase
import com.github.harmittaa.moviebrowser.db.MovieDatabase
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

interface MovieClickListener {
    fun onMovieClicked(movie: Movie)
    fun onGenreClicked(genre: Genre)
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class BrowseViewModel(
    val genreUseCase: GenreUseCase,
    val movieUseCase: MovieUseCase,
    val db: MovieDatabase
) : ViewModel(), MovieClickListener {

    private val _genres: MutableLiveData<Resource<List<Genre>>> = MutableLiveData()
    val genres: LiveData<Resource<List<Genre>>> = _genres

    private val _selectedMovie: MutableLiveData<Movie> = MutableLiveData()
    val selectedMovie: LiveData<Movie> = _selectedMovie

    val moviesOfCategory: LiveData<Resource<List<Genre>>> = _genres.switchMap { genres ->
        if (genres is Resource.Loading || genres is Resource.Error) {
            return@switchMap liveData { emit(genres) }
        }

/*
        movieUseCase.getMovies((genres as Resource.Success).data)
            .asLiveData(viewModelScope.coroutineContext)
*/
        liveData { emit(genres) }
    }

    val showLoading: LiveData<Boolean> = moviesOfCategory.map {
        it == Resource.Loading
    }

    private val _selectedGenre: MutableLiveData<Int> = MutableLiveData()
    val selectedGenre: LiveData<Int> = _selectedGenre

    fun onCreateView() {
        viewModelScope.launch {
            _genres.value = Resource.Loading
            _genres.value = genreUseCase.getGenres()
        }
    }

    override fun onMovieClicked(movie: Movie) {
        _selectedMovie.value = movie
    }

    override fun onGenreClicked(genre: Genre) {
        _selectedGenre.value = _genres.value?.data?.indexOf(genre)
    }

    fun listRefreshed() {
        _selectedMovie.value = moviesOfCategory.value?.data?.first()?.items?.first()
    }
}
