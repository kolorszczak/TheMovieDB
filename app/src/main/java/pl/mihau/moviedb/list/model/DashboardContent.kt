package pl.mihau.moviedb.list.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.mihau.moviedb.api.ListResponse

@Parcelize
data class DashboardContent(
    val nowPlaying: ListResponse<Movie>,
    val upcoming: ListResponse<Movie>,
    val popular: ListResponse<Movie>
) : Parcelable