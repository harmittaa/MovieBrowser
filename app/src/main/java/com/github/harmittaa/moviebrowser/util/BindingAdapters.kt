package com.github.harmittaa.moviebrowser.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.harmittaa.moviebrowser.domain.Movie

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, movie: Movie?) {
        if (movie == null) return
        Glide.with(view).load(movie.getBackdropPath()).into(view)
    }
}
