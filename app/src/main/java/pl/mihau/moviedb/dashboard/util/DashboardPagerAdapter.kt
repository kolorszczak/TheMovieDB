package pl.mihau.moviedb.dashboard.util

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class DashboardPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = DashboardTab.forPosition(position).createFragment()

    override fun getCount() = DashboardTab.values().size
}