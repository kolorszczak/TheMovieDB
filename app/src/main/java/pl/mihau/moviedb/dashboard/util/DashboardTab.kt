package pl.mihau.moviedb.dashboard.util

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import pl.mihau.moviedb.R
import pl.mihau.moviedb.list.view.MovieListFragment
import pl.mihau.moviedb.search.view.MovieSearchFragment

enum class DashboardTab(
    val position: Int,
    @IdRes val menuItemId: Int) {

    MOVIES(0, R.id.movies) { override fun createFragment() = MovieListFragment() },
    SEARCH(1, R.id.search) { override fun createFragment() = MovieSearchFragment() };

    abstract fun createFragment(): Fragment

    companion object {

        fun forPosition(position: Int) = values().firstOrNull { it.position == position } ?: error("Wrong position number")

        fun forMenuItemId(@IdRes menuItemId: Int) = values().firstOrNull { it.menuItemId == menuItemId } ?: error("Wrong menuItemId")
    }
}