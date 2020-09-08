package com.github.harmittaa.moviebrowser.data.uc

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.dropbox.android.external.store4.get
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.network.Resource
import java.lang.Exception
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import timber.log.Timber

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MovieUseCase(private val repository: Store<List<Genre>, List<Movie>>) {

    fun getMovies(selectedGenres: List<Genre>) = flow {
        getRepoFlow(selectedGenres)?.collect {
            Timber.d("DEBUG: EMITTINg $it")
            emit(it)
        }
    }

    private fun getRepoFlow(selectedGenres: List<Genre>): Flow<Resource<List<Movie>>>? {

        try {

            val repoStream = repository.stream(
                StoreRequest.cached(
                    emptyList(), refresh = true
                )
            ).onEach { Timber.d("DEBUG: ON EACH $it") }.transform { response ->
                Timber.d("DEBUG: Transform store response $response")
                when (response) {
                    is StoreResponse.Loading -> emit(Resource.Loading)
                    is StoreResponse.Error -> emit(
                        Resource.Error(
                            response.errorMessageOrNull() ?: "Unknown error"
                        )
                    )
                    is StoreResponse.Data -> emit(Resource.Success(response.value))
                    else -> {
                        Timber.d("ASDASDASD $response")
                    }
                }
            }
            return repoStream
        } catch (e: Exception) {
            Timber.d("EEEEE $e")
        }
        return null
    }
}
