package pl.mihau.moviedb.api

import io.reactivex.Single
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.details.model.MovieDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("movie/now_playing")
    fun getNowPlaying(@Query("page") page: Int): Single<ListResponse<Movie>>

    @GET("movie/upcoming")
    fun getUpcoming(@Query("page") page: Int): Single<ListResponse<Movie>>

    @GET("movie/popular")
    fun getPopular(@Query("page") page: Int): Single<ListResponse<Movie>>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    @GET("search/movie")
    fun searchMovie(@Query("query") phrase: String, @Query("page") page: Int): Single<ListResponse<Movie>>
}