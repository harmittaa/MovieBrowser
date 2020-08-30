package com.github.harmittaa.moviebrowser.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropbox.android.external.store4.fresh
import com.github.harmittaa.moviebrowser.data.MovieRepository
import com.github.harmittaa.moviebrowser.domain.MovieGenreLocal
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val viewModelModule = module {
    factory { BrowseViewModel(movieRepo = get()) }
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class BrowseViewModel(val movieRepo: MovieRepository) : ViewModel() {

    private val _genres: MutableLiveData<Resource<List<MovieGenreLocal>>> = MutableLiveData()
    val genres: LiveData<Resource<List<MovieGenreLocal>>> = _genres

    fun getMovieCategories() {
        viewModelScope.launch {
            _genres.value = try {
                Resource.Success(movieRepo.store.fresh(""))
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }
}
