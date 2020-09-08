package com.github.harmittaa.moviebrowser.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.harmittaa.moviebrowser.domain.GenreLocal
import com.github.harmittaa.moviebrowser.domain.GenreMovieCrossRef
import com.github.harmittaa.moviebrowser.domain.MovieLocal

private const val DB_VERSION = 1
const val DB_NAME = "MOVIE_APP_DB"

@Database(entities = [GenreLocal::class, MovieLocal::class, GenreMovieCrossRef::class], version = DB_VERSION)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
}
