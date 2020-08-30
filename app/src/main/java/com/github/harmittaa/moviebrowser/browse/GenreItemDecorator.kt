package com.github.harmittaa.moviebrowser.browse

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import com.github.harmittaa.moviebrowser.R

class GenreItemDecorator(
    @DimenRes private var startEndMargin: Int = R.dimen.margin_8,
    @DimenRes private var rightMargin: Int = R.dimen.margin_4
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = parent.resources.getDimensionPixelSize(startEndMargin)
            outRect.right = parent.resources.getDimensionPixelSize(rightMargin)
            return
        } else if (parent.getChildAdapterPosition(view) + 1 == state.itemCount) {
            outRect.right = parent.resources.getDimensionPixelSize(startEndMargin)
            return
        }

        outRect.right = parent.resources.getDimensionPixelSize(rightMargin)
    }
}
