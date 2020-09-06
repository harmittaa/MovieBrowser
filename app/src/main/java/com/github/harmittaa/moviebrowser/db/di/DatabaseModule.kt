package com.github.harmittaa.moviebrowser.db.di

import android.content.Context
import androidx.room.Room
import com.github.harmittaa.moviebrowser.db.DB_NAME
import com.github.harmittaa.moviebrowser.db.MovieDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(context = get()) }
    single { get<MovieDatabase>().genreDao() }
}

fun provideDatabase(context: Context) = Room.databaseBuilder(
    context,
    MovieDatabase::class.java, DB_NAME
).build()
