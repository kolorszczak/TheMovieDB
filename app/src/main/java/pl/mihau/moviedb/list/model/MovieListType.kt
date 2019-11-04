package pl.mihau.moviedb.list.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize
import pl.mihau.moviedb.R

@Parcelize
enum class MovieListType(@StringRes val screenTitleRes: Int) : Parcelable {

    NOW_PLAYING(R.string.title_now_playing),
    POPULAR(R.string.title_popular),
    UPCOMING(R.string.title_upcoming);
}