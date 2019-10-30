package pl.mihau.moviedb.api

import io.reactivex.Single
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.details.model.MovieDetails
import pl.mihau.moviedb.util.provider.SchedulerProvider


class MovieDBRepository(
    private val apiService: APIService,
    private val schedulerProvider: SchedulerProvider
) {

    fun getNowPlaying(page: Int = 1): Single<ListResponse<Movie>> {
        return apiService.getNowPlaying(page)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }

    fun getUpcoming(page: Int = 1): Single<ListResponse<Movie>> {
        return apiService.getUpcoming(page)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }

    fun getPopular(page: Int = 1): Single<ListResponse<Movie>> {
        return apiService.getPopular(page)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }

    fun getMovieDetails(id: Int): Single<MovieDetails> {
        return apiService.getMovieDetails(id)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }

    fun searchMovie(phrase: String, page: Int): Single<ListResponse<Movie>> {
        return apiService.searchMovie(phrase = phrase, page = page)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}