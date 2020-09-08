package com.github.harmittaa.moviebrowser.db

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.koin.java.KoinJavaComponent

class Converters {
    private val moshi: Moshi by KoinJavaComponent.inject(Moshi::class.java)
    private val listConverter: JsonAdapter<List<Int>>

    init {
        listConverter = moshi.adapter(Types.newParameterizedType(List::class.java, Integer::class.java))
    }

    @TypeConverter
    fun fromList(value: String) = listConverter.fromJson(value)

    @TypeConverter
    fun toList(value: List<Int>) = listConverter.toJson(value)
}
