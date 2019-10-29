package pl.mihau.moviedb.util.extension

import android.content.Intent
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
fun <T : Serializable> Intent?.requiredSerializable(key: String): T {
    return getSerializable(key) as T
}

private fun Intent?.getSerializable(key: String) = getIntent().getSerializableExtra(key)
    ?: error("$key key must be provided.")

private fun Intent?.getIntent() = this ?: error("Intent is required but was null")