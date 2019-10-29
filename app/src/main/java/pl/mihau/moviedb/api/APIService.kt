package pl.mihau.moviedb.api

import io.reactivex.Single
import pl.mihau.moviedb.movielist.model.Movie
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("movie/now_playing")
    fun getNowPlaying(): Single<Response<Movie>>

    @GET("movie/upcoming")
    fun getUpcoming(): Single<Response<Movie>>

    @GET("movie/popular")
    fun getPopular(): Single<Response<Movie>>

    @GET("search/upcoming")
    fun searchMovie(@Query("query") phrase: String): Single<Response<Movie>>
}