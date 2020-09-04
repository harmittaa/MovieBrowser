package com.github.harmittaa.moviebrowser.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.github.harmittaa.moviebrowser.data.uc.GenreUseCase
import com.github.harmittaa.moviebrowser.data.uc.MovieUseCase
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.domain.MovieGenreLocal
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

interface MovieClickListener {
    fun onMovieClicked(movie: Movie)
    fun onGenreClicked(genre: MovieGenreLocal)
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class BrowseViewModel(
    val genreUseCase: GenreUseCase,
    val movieUseCase: MovieUseCase
) : ViewModel(), MovieClickListener {

    private val _genres: MutableLiveData<Resource<List<MovieGenreLocal>>> = MutableLiveData()
    val genres: LiveData<Resource<List<MovieGenreLocal>>> = _genres

    private val _selectedMovie: MutableLiveData<Movie> = MutableLiveData()
    val selectedMovie: LiveData<Movie> = _selectedMovie

    val moviesOfCategory = _genres.switchMap { genres ->
        if (genres is Resource.Loading || genres is Resource.Error) {
            return@switchMap liveData { emit(genres) }
        }

        movieUseCase.getMovies((genres as Resource.Success).data)
            .asLiveData(viewModelScope.coroutineContext)
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

    override fun onGenreClicked(genre: MovieGenreLocal) {
        _selectedGenre.value = _genres.value?.data?.indexOf(genre)
    }

    fun listRefreshed() {
        _selectedMovie.value = moviesOfCategory.value?.data?.first()?.movies?.first()
    }
}
