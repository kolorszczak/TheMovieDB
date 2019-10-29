package pl.mihau.moviedb.common

import android.Manifest

object Values {
    const val INTERCEPTOR_CONTENT_TYPE: String = "application/json;charset=utf-8"
    const val INTERCEPTOR_AUTHORIZATION: String =
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmZjc2NTBmYTIyODkxMmJhYTdiZWI4MWE2MzdlNjExMiIsInN1YiI6IjVkYjc0NmEzMDc5MmUxMDAxODQzMjdlYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.eEIVUwhZTNJQ60qEG_4ywGvpnL8-sJxijxlBOMHAI7I"
    const val IMAGES_PREFIX: String = "https://image.tmdb.org/t/p/original/"
}

object Permissions {
    val requiredPermissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE
    )
}
