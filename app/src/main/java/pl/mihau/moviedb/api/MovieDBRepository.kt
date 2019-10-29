package pl.mihau.moviedb.api

import io.reactivex.Single
import pl.mihau.moviedb.movielist.model.Movie
import pl.mihau.moviedb.util.provider.SchedulerProvider


class MovieDBRepository(
    private val apiService: APIService,
    private val schedulerProvider: SchedulerProvider
) {

    fun getNowPlaying(): Single<Response<Movie>> {
        return apiService.getNowPlaying()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }

    fun getUpcoming(): Single<Response<Movie>> {
        return apiService.getUpcoming()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }

    fun getPopular(): Single<Response<Movie>> {
        return apiService.getPopular()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }

    fun searchMovie(phrase: String): Single<Response<Movie>> {
        return apiService.searchMovie(phrase = phrase)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}