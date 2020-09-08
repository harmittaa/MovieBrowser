package com.github.harmittaa.moviebrowser.data.uc

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.get
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class MovieUseCase(private val repository: Store<Genre, List<Movie>>) {

/*
    fun getMovies(genres: List<GenreLocal>): Flow<Resource<List<GenreLocal>>> {
        return flow {
            try {
                val mapped = genres.map { genre ->
                    genre.movies = repository.fresh(genre.id)
                    genre
                }
                Timber.d("EMIT FROM USE CASE!")
                emit(Resource.Success(mapped))
            }
        } catch (e: Exception) {
            Timber.e("Failed to $e")
            emit(Resource.Error(e))
        }
    }

    fun getMoviesChanged(genres: List<Genre>): Flow<Resource<List<Genre>>> = flow {
        try {
            val mapped = genres.map { genre ->
                repository.stream(StoreRequest.cached(genre, refresh = true)).collect { response ->
                    if (response.dataOrNull() == null) {
                        Timber.d("STILL NULL")
                    } else {
                        Timber.d("Genre items are $response.requireData()")
                        genre.movies = response.requireData()
                    }
                }
                Timber.d("Genre completed $genre")
                genre
            }
            Timber.d("EMIT FROM USE CASE!")
            emit(Resource.Success(mapped))
        } catch (e: Exception) {
            Timber.e("Failed to $e")
            emit(Resource.Error(e))
        }
    }
*/

    fun getMoviesChanged(genres: List<Genre>): Flow<Resource<List<Genre>>> = flow {
        try {
            val mapped = genres.map { genre ->
                repository.stream(StoreRequest.cached(genre, refresh = true)).collect { response ->
                    if (response.dataOrNull() == null) {
                        Timber.d("STILL NULL")
                    } else {
                        Timber.d("Genre items are $response.requireData()")
                        genre.items = response.requireData()
                    }
                }
                Timber.d("Genre completed $genre")
                genre
            }
            Timber.d("EMIT FROM USE CASE!")
            emit(Resource.Success(mapped))
        } catch (e: Exception) {
            Timber.e("Failed to $e")
            emit(Resource.Error(e))
        }
    }
}
