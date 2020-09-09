package com.github.harmittaa.moviebrowser.browse

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.github.harmittaa.moviebrowser.data.uc.GenreUseCase
import com.github.harmittaa.moviebrowser.data.uc.MovieUseCase
import com.github.harmittaa.moviebrowser.db.MovieDatabase
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.GenreLocal
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber

interface MovieClickListener {
    fun onMovieClicked(view: View, movie: Movie)
    fun onGenreClicked(view: View, genre: Genre)
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class BrowseViewModel(
    val genreUseCase: GenreUseCase,
    val movieUseCase: MovieUseCase,
    val db: MovieDatabase
) : ViewModel(), MovieClickListener {

    private val selectedGenres = getDefaultGenreList()

    private val _genres: LiveData<List<Genre>> =
        genreUseCase.getGenres().asLiveData(viewModelScope.coroutineContext)
    val genres: LiveData<List<Genre>> = _genres

    private val _selectedMovie: MutableLiveData<Movie> = MutableLiveData()
    val selectedMovie: LiveData<Movie> = _selectedMovie

    val moviesOfCategory: LiveData<Resource<List<Movie>>> = _genres.switchMap { genres ->
        movieUseCase.getMovies(genres).asLiveData(viewModelScope.coroutineContext)
    }

    val showLoading: LiveData<Boolean> = moviesOfCategory.map {
        it == Resource.Loading
    }

    private val _selectedGenre: MutableLiveData<List<Genre>> = MutableLiveData()
    val selectedGenre: LiveData<List<Genre>> = _selectedGenre

    fun onCreateView() {
        prepopulateDB()
    }

    override fun onMovieClicked(view: View, movie: Movie) {
        _selectedMovie.value = movie
    }

    override fun onGenreClicked(view: View, genre: Genre) {
    }

    fun listRefreshed() {
        if (moviesOfCategory.value != null || !moviesOfCategory.value?.data.isNullOrEmpty()) {
            return
        }
        _selectedMovie.value = moviesOfCategory.value?.data?.first()
    }

    private fun getDefaultGenreList() = mapOf(
        28 to "Action",
        12 to "Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        1075 to "Family",
        14 to "Fantasy",
        36 to "History",
        27 to "Horror",
        10402 to "Music",
        9648 to "Mystery",
        10749 to "Romance",
        878 to "Science Fiction",
        10770 to "TV Movie",
        53 to "Thriller",
        19752 to "War",
        37 to "Western"
    )

    private fun prepopulateDB() {
        val genresList = mutableListOf<GenreLocal>()
        getDefaultGenreList().forEach { id, name ->
            genresList.add(GenreLocal(id, name))
        }
/*
        viewModelScope.launch(Dispatchers.IO) {
            db.genreDao().insertGenres(*genresList.toTypedArray())
        }
*/
    }

    fun onGenresFetched() {
        viewModelScope.launch(Dispatchers.IO) {
            movieUseCase.getMovies(_genres.value!!).flowOn(Dispatchers.IO).collect {
                Timber.d("DEBUG: ON VM $it")
            }
        }
    }
}
