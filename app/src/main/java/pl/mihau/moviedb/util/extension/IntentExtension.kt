package pl.mihau.moviedb.util.extension

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable

fun Intent?.requireLong(key: String) = getIntent().getLongExtra(key, Long.MIN_VALUE).takeIf { it != Long.MIN_VALUE } ?: error("$key's Long value is required but was null")

fun <T : Parcelable> Intent?.requiredParcelable(key: String) = getIntent().getParcelableExtra<T>(key)
    ?: error("$key key must be provided.")

fun <T : Parcelable> Bundle?.requiredParcelable(key: String) = getArgs().getParcelable<T>(key)
    ?: error("$key key must be provided.")

private fun Intent?.getIntent() = this ?: error("Intent is required but was null")

private fun Bundle?.getArgs() = this ?: error("Intent is required but was null")