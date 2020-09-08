package com.github.harmittaa.moviebrowser.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.harmittaa.moviebrowser.domain.Genre
import com.github.harmittaa.moviebrowser.domain.GenreLocal
import com.github.harmittaa.moviebrowser.domain.GenreMovieCrossRef
import com.github.harmittaa.moviebrowser.domain.GenreWithMovies
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.domain.MovieLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

@Dao
abstract class GenreDao {

    fun getGenresMovies(genre: Genre): Flow<List<Movie>> {
        val result = getGenreWithMovies(genre.genreId)

        val movieFlow = result.map {
            if (it == null) {
                Timber.d("Reading with $genre results in null result")
                emptyList()
            } else {
                it.movies
            }
        }
        return movieFlow
    }

    @Transaction
    open suspend fun insertGenresMovies(genre: Genre, movies: List<Movie>) {
        Timber.d("Storing data $genre with $movies")
        insertGenre(GenreLocal(genre.genreId, genre.name))
        movies.forEach {
            insertMovie(
                MovieLocal(
                    it.movieId,
                    it.title,
                    it.overview,
                    it.backdropUrl,
                    it.posterUrl,
                    it.rating,
                    it.genreIds
                )
            )
            insertCrossRef(GenreMovieCrossRef(genre.genreId, it.movieId))
        }
    }

    suspend fun deleteGenre(genre: Genre) {
        deleteMoviesOfGenre(genre.genreId)
    }

    @Query("DELETE FROM genre WHERE genreId = :genreId")
    abstract suspend fun deleteMoviesOfGenre(genreId: Int)

    @Query("DELETE FROM genre")
    abstract suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM genre WHERE genreId = :genreId")
    abstract fun getGenreWithMovies(genreId: Int): Flow<GenreWithMovies?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertGenre(genre: GenreLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMovie(movie: MovieLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCrossRef(crossRef: GenreMovieCrossRef)

    @Query("SELECT * FROM movie")
    abstract fun getMovies(): Flow<List<MovieLocal>>
}
