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

@Dao
abstract class GenreDao {

    fun getMoviesOfGenres(genres: List<Genre>): Flow<List<Movie>?> =
        if (genres.isEmpty()) {
            getAllMovies().map { movieList ->
                movieList.sortedByDescending { it.rating }
            }
        } else {
            val genreIds = genres.map { it.genreId }
            queryMoviesOfGenres(genreIds).map { genresWithMovies ->
                val movies = mutableSetOf<Movie>()

                genresWithMovies?.forEach { movies.addAll(it?.movies ?: emptyList()) }
                var filteredMovies = movies.filter { it.genreIds.containsAll(genreIds) }
                filteredMovies = filteredMovies.sortedByDescending { it.rating }

                if (movies.isEmpty()) {
                    null
                } else {
                    filteredMovies
                }
            }
        }

    @Transaction
    open suspend fun insertGenresMovies(genre: List<Genre>, movies: List<MovieDto>) {

        val crossRefs = mutableListOf<GenreMovieCrossRef>()
        val toLocalMapped = movies.map {
            it.toLocal()
        }

        toLocalMapped.forEach { movie ->
            movie.genreIds.forEach { genreId ->
                crossRefs.add(GenreMovieCrossRef(genreId, movie.movieId))
            }
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

    @Query("SELECT * FROM movie")
    abstract fun getAllMovies(): Flow<List<MovieLocal>>

    @Query("DELETE FROM movie")
    abstract suspend fun deleteAll()

    @Query("DELETE FROM movie WHERE genreIds IN (:genreId)")
    protected abstract suspend fun deleteMoviesOfGenreIds(genreId: List<Int>)

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
