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
import com.github.harmittaa.moviebrowser.domain.MovieDto
import com.github.harmittaa.moviebrowser.domain.MovieLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

@Dao
abstract class GenreDao {

    fun getMoviesOfGenres(genres: List<Genre>): Flow<List<Movie>?> =
        if (genres.isEmpty()) {
            getAllMovies()
        } else {
            queryMoviesOfGenres(genres.map { it.genreId }).map { genresWithMovies ->
                val movies = mutableListOf<Movie>()

                genresWithMovies?.forEach { movies + (it?.movies ?: emptyList()) }

                if (movies.isEmpty()) {
                    null
                } else {
                    movies
                }
            }
        }

    @Transaction
    open suspend fun insertGenresMovies(genre: List<Genre>, movies: List<Movie>) {
        Timber.d("Storing data $genre with $movies")

        val crossRefs = mutableListOf<GenreMovieCrossRef>()
        val toLocalMapped = (movies as List<MovieDto>).map {
            crossRefs.add(GenreMovieCrossRef(123, 123))
            it.toLocal()
        }

        insertGenreMovieCrossRefs(*crossRefs.toTypedArray())
        insertMovies(*toLocalMapped.toTypedArray())
    }

    suspend fun deleteMoviesOfGenre(genres: List<Genre>) {
        deleteMoviesOfGenreIds(genres.map { it.genreId })
    }

    @Transaction
    @Query("SELECT * FROM genre WHERE genreId IN (:genreId)")
    abstract fun queryMoviesOfGenres(genreId: List<Int>): Flow<List<GenreWithMovies?>?>

    @Query("DELETE FROM movie WHERE genreIds IN (:genreId)")
    protected abstract suspend fun deleteMoviesOfGenreIds(genreId: List<Int>)

    @Query("SELECT * FROM movie")
    abstract fun getAllMovies(): Flow<List<MovieLocal>>

    @Query("DELETE FROM movie")
    abstract suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMovies(vararg movies: MovieLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCrossRef(crossRef: GenreMovieCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertGenreMovieCrossRefs(vararg lists: GenreMovieCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertGenres(vararg genres: GenreLocal)

    @Query("SELECT * FROM genre")
    abstract fun getGenres(): Flow<List<GenreLocal>>
}
