package pl.mihau.moviedb.util.list

import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class HorizontalSpaceItemDecoration(@DimenRes private val spaceInDp: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val isLastItem = parent.getChildAdapterPosition(view) == state.itemCount - 1

        when {
            isLastItem -> {
                outRect.right = parent.resources.getDimensionPixelSize(spaceInDp)
                outRect.left = parent.resources.getDimensionPixelSize(spaceInDp)
            }
            else -> outRect.left = parent.resources.getDimensionPixelSize(spaceInDp)
        }
    }
}