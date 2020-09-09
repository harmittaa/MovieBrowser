package com.github.harmittaa.moviebrowser.domain

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

abstract class Genre {
    abstract val genreId: Int
    abstract val name: String
}

@Entity(primaryKeys = ["genreId", "movieId"])
data class GenreMovieCrossRef(
    val genreId: Int,
    val movieId: Int
)

data class GenreWithMovies(
    @Embedded val genre: GenreLocal,
    @Relation(
        parentColumn = "genreId",
        entityColumn = "movieId",
        associateBy = Junction(GenreMovieCrossRef::class)
    )
    val movies: List<MovieLocal>
)

@JsonClass(generateAdapter = true)
data class GenreDto(
    @Json(name = "id") val genreId: Int,
    val name: String
)

@Entity(tableName = "genre")
data class GenreLocal(
    @PrimaryKey
    @ColumnInfo(name = "genreId")
    override val genreId: Int,
    @ColumnInfo(name = "name")
    override val name: String
) : Genre()
