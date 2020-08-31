package com.github.harmittaa.moviebrowser.network

// from
// https://github.com/dropbox/Store/blob/main/app/src/main/java/com/dropbox/android/sample/utils/Lce.kt
sealed class Resource<out T> {

    open val data: T? = null

    abstract fun <R> map(f: (T) -> R): Resource<R>

    inline fun doOnData(f: (T) -> Unit) {
        if (this is Success) {
            f(data)
        }
    }

    data class Success<out T>(override val data: T) : Resource<T>() {
        override fun <R> map(f: (T) -> R): Resource<R> = Success(f(data))
    }

    data class Error(val message: String) : Resource<Nothing>() {
        constructor(t: Throwable) : this(t.message ?: "")

        override fun <R> map(f: (Nothing) -> R): Resource<R> = this
    }

    object Loading : Resource<Nothing>() {
        override fun <R> map(f: (Nothing) -> R): Resource<R> = this
    }
}
