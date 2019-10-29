package pl.mihau.moviedb.util.extension

import android.app.Activity
import android.content.Intent

fun Activity.startActivityWithFinish(intent: Intent) {
    startActivity(intent)
    finish()
}

fun Activity.requireContext() = baseContext!!