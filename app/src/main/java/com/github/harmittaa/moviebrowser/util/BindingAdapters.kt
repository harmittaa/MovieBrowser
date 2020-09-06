package com.github.harmittaa.moviebrowser.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.harmittaa.moviebrowser.R
import com.github.harmittaa.moviebrowser.domain.Movie
import timber.log.Timber

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, movie: Movie?) {
        if (movie == null) return
        Glide.with(view)
            .load(movie.getBackdropPath())
            .placeholder(R.drawable.placeholder_drawable)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        Timber.d("visibleGone($view, $show)")
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}
