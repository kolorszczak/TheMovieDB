package pl.mihau.moviedb.common

object Strings {
    const val empty = ""
}

object Codes {
    const val MOVIE_DETAILS = 1
}

object Keys {
    const val FAVORITES = "key_FAVORITES"
    const val MOVIE = "key_MOVIE"
    const val MOVIE_ID = "key_MOVIE_ID"
    const val LIST_TYPE = "key_LIST_TYPE"
    const val SHARED_PREFS = "key_SHARED_PREFS"
}

object Values {
    const val INTERCEPTOR_CONTENT_TYPE: String = "application/json;charset=utf-8"
    const val INTERCEPTOR_AUTHORIZATION: String = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmZjc2NTBmYTIyODkxMmJhYTdiZWI4MWE2MzdlNjExMiIsInN1YiI6IjVkYjc0NmEzMDc5MmUxMDAxODQzMjdlYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.eEIVUwhZTNJQ60qEG_4ywGvpnL8-sJxijxlBOMHAI7I"
    const val IMAGES_PREFIX: String = "https://image.tmdb.org/t/p/w500/"
    const val SPLASH_TIME_IN_MILLIS: Long = 2000
    const val TIMEOUT_IN_SEC: Long = 10
}
