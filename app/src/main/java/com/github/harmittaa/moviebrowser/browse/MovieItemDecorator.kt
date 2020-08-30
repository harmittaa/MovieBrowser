package com.github.harmittaa.moviebrowser.browse

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.github.harmittaa.moviebrowser.R

class MovieItemDecorator(
    @DimenRes private var topBottomMargin: Int = R.dimen.margin_8,
    @DimenRes private var bottomMargin: Int = R.dimen.margin_4
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = parent.resources.getDimensionPixelSize(topBottomMargin)
            outRect.bottom = parent.resources.getDimensionPixelSize(bottomMargin)
            return
        } else if (parent.getChildAdapterPosition(view) + 1 == state.itemCount) {
            outRect.bottom = parent.resources.getDimensionPixelSize(topBottomMargin)
            return
        }

        outRect.bottom = parent.resources.getDimensionPixelSize(bottomMargin)
    }
}
