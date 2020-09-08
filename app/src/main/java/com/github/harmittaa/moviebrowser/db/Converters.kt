package com.github.harmittaa.moviebrowser.db

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.koin.java.KoinJavaComponent

/*
internal class SiteLocalConverter {
    private val moshi: Moshi by inject(Moshi::class.java)
    private val categoriesConverter: JsonAdapter<List<SiteCategory>>
    private val imageSetsConverter: JsonAdapter<List<Image.Set>>

    init {
        val categoriesType = Types.newParameterizedType(List::class.java, SiteCategory::class.java)
        categoriesConverter = moshi.adapter(categoriesType)

        val imageSetsType = Types.newParameterizedType(List::class.java, Image.Set::class.java)
        imageSetsConverter = moshi.adapter(imageSetsType)
    }

    @TypeConverter
    fun toImageSets(value: String) = imageSetsConverter.fromJson(value)

    @TypeConverter
    fun fromImageSets(value: List<Image.Set>): String = imageSetsConverter.toJson(value)

    @TypeConverter
    fun toCategories(value: String) = categoriesConverter.fromJson(value)

    @TypeConverter
    fun fromCategories(value: List<SiteCategory>): String = categoriesConverter.toJson(value)
}

 */

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
