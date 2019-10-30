package pl.mihau.moviedb.util.extension

import android.content.Intent
import android.os.Parcelable

fun <T : Parcelable> Intent?.requiredParcelable(key: String) = getIntent().getParcelableExtra<T>(key)
    ?: error("$key key must be provided.")

private fun Intent?.getIntent() = this ?: error("Intent is required but was null")