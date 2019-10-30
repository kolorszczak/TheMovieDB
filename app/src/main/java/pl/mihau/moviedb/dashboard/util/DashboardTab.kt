package pl.mihau.moviedb.dashboard.util

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import pl.mihau.moviedb.R
import pl.mihau.moviedb.list.view.MovieListFragment
import pl.mihau.moviedb.search.view.MovieSearchFragment

enum class DashboardTab(
    val position: Int,
    @IdRes val menuItemId: Int) {

    MOVIES(0, R.id.movies) {

        override fun createFragment() = MovieListFragment()
    },
    SEARCH(1, R.id.search) {

        override fun createFragment() = MovieSearchFragment()
    };

    abstract fun createFragment(): Fragment

    companion object {

        fun forPosition(position: Int): DashboardTab {
            for (tab: DashboardTab in DashboardTab.values()) {
                if (tab.position == position) {
                    return tab
                }
            }
            error("Unknown $this for position=$position")
        }

        fun forMenuItemId(@IdRes menuItemId: Int): DashboardTab {
            for (tab: DashboardTab in values()) {
                if (tab.menuItemId == menuItemId) {
                    return tab
                }
            }

            error("Unknown $this for position=$menuItemId")
        }
    }
}