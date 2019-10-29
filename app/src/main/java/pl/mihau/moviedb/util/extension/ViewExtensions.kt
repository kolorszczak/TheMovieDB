package pl.mihau.moviedb.util.extension

import android.view.View

fun View.setVisibility(value: Boolean) {
    this.visibility = if (value) View.VISIBLE else View.GONE
}