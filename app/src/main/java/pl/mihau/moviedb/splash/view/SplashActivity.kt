package pl.mihau.moviedb.splash.view

import android.os.Bundle
import io.reactivex.Single
import pl.mihau.moviedb.R
import pl.mihau.moviedb.common.Values
import pl.mihau.moviedb.common.view.BaseActivity
import pl.mihau.moviedb.dashboard.view.DashboardActivity
import pl.mihau.moviedb.databinding.ActivitySplashBinding
import pl.mihau.moviedb.util.databinding.contentView
import pl.mihau.moviedb.util.extension.startActivityWithFinish
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    private val binding by contentView<SplashActivity, ActivitySplashBinding>(R.layout.activity_splash)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this

        startApp()
    }

    private fun startApp() = Single.timer(Values.SPLASH_TIME_IN_MILLIS, TimeUnit.MILLISECONDS)
        .map { openDashboard() }
        .subscribe()

    private fun openDashboard() = startActivityWithFinish(DashboardActivity.intent(this))
}
