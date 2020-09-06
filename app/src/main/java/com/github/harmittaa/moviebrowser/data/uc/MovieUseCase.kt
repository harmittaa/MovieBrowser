package com.github.harmittaa.moviebrowser.data.uc

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.fresh
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.domain.GenreLocal
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class MovieUseCase(private val repository: Store<Int, List<Movie>>) {

    fun getMovies(genres: List<GenreLocal>): Flow<Resource<List<GenreLocal>>> {
        return flow {
            try {
                val mapped = genres.map { genre ->
                    genre.movies = repository.fresh(genre.id)
                    genre
                }
                Timber.d("EMIT FROM USE CASE!")
                emit(Resource.Success(mapped))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }
}
