package com.github.harmittaa.moviebrowser.data.uc

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.fresh
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.domain.MovieGenreLocal
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieUseCase(val repository: Store<Int, List<Movie>>) {

    fun getMovies(genres: List<MovieGenreLocal>): Flow<Resource<List<MovieGenreLocal>>> {
        return flow {
            try {
                val mapped = genres.map { genre ->
                    genre.items = repository.fresh(genre.id)
                    genre
                }
                emit(Resource.Success(mapped))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }
        }
    }
}