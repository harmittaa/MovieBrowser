package com.github.harmittaa.moviebrowser.domain.uc

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.network.Resource
import java.lang.Exception
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import timber.log.Timber

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MovieUseCase(
    private val repository: Store<List<Genre>, List<Movie>>,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getMovies(selectedGenres: List<Genre>) = flow {
        getRepoFlow(selectedGenres).collect {
            emit(it)
        }
    }.flowOn(coroutineDispatcher)

    private fun getRepoFlow(selectedGenres: List<Genre>): Flow<Resource<List<Movie>>> {
        try {
            return repository.stream(
                StoreRequest.cached(
                    selectedGenres, refresh = true
                )
            ).transform { response ->
                when (response) {
                    is StoreResponse.Loading -> emit(Resource.Loading)
                    is StoreResponse.Error -> emit(
                        Resource.Error(
                            response.errorMessageOrNull() ?: "Unknown error"
                        )
                    )
                    is StoreResponse.Data -> emit(Resource.Success(response.value))
                }
            }
        } catch (e: Exception) {
            Timber.d("Exception thrown from repo $e")
            return flowOf(Resource.Error(e.message ?: "Unknown error"))
        }
    }
}
