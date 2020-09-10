package com.github.harmittaa.moviebrowser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.harmittaa.moviebrowser.db.MovieDatabase
import com.github.harmittaa.moviebrowser.domain.GenreLocal
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class MainActivity : AppCompatActivity() {
    private val db: MovieDatabase by inject(MovieDatabase::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        prepopulateDb()
    }

    private fun prepopulateDb() {
        val genresList = mutableListOf<GenreLocal>()
        defaultGenreList.forEach { (id, name) ->
            genresList.add(GenreLocal(id, name))
        }

        CoroutineScope(EmptyCoroutineContext).launch(Dispatchers.IO) {
            db.genreDao().insertGenres(*genresList.toTypedArray())
        }
    }

    private val defaultGenreList = mapOf(
        28 to "Action",
        12 to "Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        1075 to "Family",
        14 to "Fantasy",
        36 to "History",
        27 to "Horror",
        10402 to "Music",
        9648 to "Mystery",
        10749 to "Romance",
        878 to "Science Fiction",
        10770 to "TV Movie",
        53 to "Thriller",
        19752 to "War",
        37 to "Western"
    )
}
