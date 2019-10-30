package pl.mihau.moviedb.details.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String?,
    @SerializedName("status") val status: String,
    @SerializedName("adult") val forAdult: Boolean,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("popularity") val popularity: Number,
    @SerializedName("budget") val budget: Int,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("revenue") val revenue: Int,
    @SerializedName("video") val hasVideo: Boolean,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("vote_average") val voteAverage: Number,
    @SerializedName("vote_count") val voteCount: Int
) : Parcelable