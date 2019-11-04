package pl.mihau.moviedb.dashboard.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dashboard.*
import pl.mihau.moviedb.R
import pl.mihau.moviedb.common.view.BaseActivity
import pl.mihau.moviedb.dashboard.util.DashboardPagerAdapter
import pl.mihau.moviedb.dashboard.util.DashboardTab
import pl.mihau.moviedb.databinding.ActivityDashboardBinding
import pl.mihau.moviedb.util.databinding.contentView

class DashboardActivity : BaseActivity() {

    private val binding by contentView<DashboardActivity, ActivityDashboardBinding>(R.layout.activity_dashboard)

    private val adapter = DashboardPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this

        setupViewPager()
        setupBottomNavigationView()
    }

    private fun setupViewPager() {
        viewPager.also {
            it.adapter = adapter
            it.offscreenPageLimit = adapter.count
        }
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            DashboardTab.forMenuItemId(it.itemId).let { tab ->
                binding.viewPager.setCurrentItem(tab.position, true)
                true
            }
        }
    }

    companion object {
        fun intent(context: Context) = Intent(context, DashboardActivity::class.java)
    }
}