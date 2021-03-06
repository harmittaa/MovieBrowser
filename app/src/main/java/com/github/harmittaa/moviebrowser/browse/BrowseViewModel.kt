package com.github.harmittaa.moviebrowser.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.github.harmittaa.moviebrowser.data.uc.GenreUseCase
import com.github.harmittaa.moviebrowser.data.uc.MovieUseCase
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

interface GenreClickListener {
    fun onGenreClicked(genre: Genre)
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class BrowseViewModel(
    genreUseCase: GenreUseCase,
    private val movieUseCase: MovieUseCase
) : ViewModel(), GenreClickListener {

    private val _genres: LiveData<List<Genre>> =
        genreUseCase.getGenres().asLiveData(viewModelScope.coroutineContext)
    val genres: LiveData<List<Genre>> = _genres

    private val shouldFetchSites: MutableLiveData<Unit> = MutableLiveData()

    private val _selectedGenres: MutableLiveData<MutableSet<Genre>> = MutableLiveData()
    val selectedGenres: LiveData<MutableSet<Genre>> = _selectedGenres

    private val genreInputFilter = MediatorLiveData<List<Genre>>()

    val moviesOfCategory: LiveData<Resource<List<Movie>>> = genreInputFilter.switchMap { genres ->
        movieUseCase.getMovies(genres).asLiveData(viewModelScope.coroutineContext)
    }

    val showLoading: LiveData<Boolean> = moviesOfCategory.map {
        it == Resource.Loading
    }

    init {
        genreInputFilter.addSource(shouldFetchSites) {
            genreInputFilter.value = emptyList()
        }
        shouldFetchSites.value = Unit
        genreInputFilter.addSource(selectedGenres) {
            genreInputFilter.value = it.toList()
        }
    }

    override fun onGenreClicked(genre: Genre) {
        val list = selectedGenres.value ?: mutableSetOf()
        val addResult = list.add(genre)
        if (!addResult) {
            list.remove(genre)
        }
        _selectedGenres.value = list
    }

    fun clearFilters() {
        _selectedGenres.value = mutableSetOf()
    }
}
