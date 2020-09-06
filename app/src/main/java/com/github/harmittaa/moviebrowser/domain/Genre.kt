package com.github.harmittaa.moviebrowser.domain

import androidx.room.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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
    val movies: List<MovieLocal> = listOf()
) {
    constructor(genre: GenreLocal) : this(genre, listOf())
}


abstract class Genre {
    abstract val genreId: Int
    abstract val name: String
    abstract var items: List<Movie>?
}

@JsonClass(generateAdapter = true)
data class GenreDto(
    @Json(name = "id") val genreId: Int,
    val name: String
) {
    fun toLocal() = GenreLocal(
        genreId = genreId,
        name = name,
        items = null
    )
}

@Entity(tableName = "genre")
data class GenreLocal(
    @PrimaryKey
    @ColumnInfo(name = "genreId")
    override val genreId: Int,
    @ColumnInfo(name = "name")
    override val name: String,
    @Ignore
    override var items: List<Movie>? = null
) : Genre() {
    constructor(genreId: Int, name: String) : this(genreId, name, null)
}
