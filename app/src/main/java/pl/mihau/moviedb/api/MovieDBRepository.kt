package pl.mihau.moviedb.api

import io.reactivex.Single
import pl.mihau.moviedb.details.model.MovieDetails
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.util.extension.schedule
import pl.mihau.moviedb.util.provider.SchedulerProvider

class MovieDBRepository(
    private val apiService: APIService,
    private val schedulerProvider: SchedulerProvider
) {

    fun getNowPlaying(page: Int = 1): Single<ListResponse<Movie>> =
        apiService.getNowPlaying(page).schedule(schedulerProvider)

    fun getUpcoming(page: Int = 1): Single<ListResponse<Movie>> =
        apiService.getUpcoming(page).schedule(schedulerProvider)

    fun getPopular(page: Int = 1): Single<ListResponse<Movie>> =
        apiService.getPopular(page).schedule(schedulerProvider)

    fun getMovieDetails(id: Int): Single<MovieDetails> =
        apiService.getMovieDetails(id).schedule(schedulerProvider)

    fun searchMovie(phrase: String, page: Int): Single<ListResponse<Movie>> =
        apiService.searchMovie(phrase, page).schedule(schedulerProvider)
}