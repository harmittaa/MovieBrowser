package com.github.harmittaa.moviebrowser.browse

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
import kotlinx.coroutines.launch
import timber.log.Timber

interface MovieClickListener {
    fun onGenreClicked(view: View, genre: Genre)
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class BrowseViewModel(
    val genreUseCase: GenreUseCase,
    val movieUseCase: MovieUseCase,
    val db: MovieDatabase
) : ViewModel(), MovieClickListener {

    private val _genres: LiveData<List<Genre>> =
        genreUseCase.getGenres().asLiveData(viewModelScope.coroutineContext)
    val genres: LiveData<List<Genre>> = _genres

    private val selectedGenres: MutableLiveData<MutableSet<Genre>> = MutableLiveData()

    private val genreInputFilter = MediatorLiveData<List<Genre>>()

    private val _selectedMovie: MutableLiveData<Movie> = MutableLiveData()
    val selectedMovie: LiveData<Movie> = _selectedMovie

    val moviesOfCategory: LiveData<Resource<List<Movie>>> = genreInputFilter.switchMap { genres ->
        movieUseCase.getMovies(genres).asLiveData(viewModelScope.coroutineContext)
    }

    private val _selectedGenre: MutableLiveData<List<Genre>> = MutableLiveData()
    val selectedGenre: LiveData<List<Genre>> = _selectedGenre

    init {
        genreInputFilter.addSource(_genres) {
            Timber.d("Mediator added genres! $it")
            if (_genres.value?.size ?: 0 != getDefaultGenreList().size) {
                genreInputFilter.value = it
            } else {
                if (genreInputFilter.value == null) {
                    genreInputFilter.value = emptyList()
                }
            }
        }
        genreInputFilter.addSource(selectedGenres) {
            Timber.d("Mediator selected genres! $it")
            genreInputFilter.value = it.toList()
        }
    }

    override fun onGenreClicked(view: View, genre: Genre) {
        val list = selectedGenres.value ?: mutableSetOf()
        val addResult = list.add(genre)
        if (!addResult) {
            list.remove(genre)
        }
        selectedGenres.value = list
    }

    fun onCreateView() {
        prepopulateDB()
    }

    private fun prepopulateDB() {
        val genresList = mutableListOf<GenreLocal>()
        getDefaultGenreList().forEach { (id, name) ->
            genresList.add(GenreLocal(id, name))
        }
        viewModelScope.launch(Dispatchers.IO) {
            db.genreDao().insertGenres(*genresList.toTypedArray())
        }
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
}
