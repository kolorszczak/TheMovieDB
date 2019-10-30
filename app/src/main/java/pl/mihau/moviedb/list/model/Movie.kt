package pl.mihau.moviedb.list.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String?,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("popularity") val popularity: Number,
    @SerializedName("vote_average") val voteAverage: Number,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("original_language") val language: String,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("video") val hasVideo: Boolean,
    @SerializedName("adult") val forAdult: Boolean
) : Parcelable