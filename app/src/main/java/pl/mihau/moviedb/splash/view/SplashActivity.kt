package pl.mihau.moviedb.splash.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import pl.mihau.moviedb.R
import pl.mihau.moviedb.common.Values
import pl.mihau.moviedb.common.view.BaseActivity
import pl.mihau.moviedb.dashboard.view.DashboardActivity
import pl.mihau.moviedb.databinding.ActivitySplashBinding
import pl.mihau.moviedb.util.databinding.contentView
import pl.mihau.moviedb.util.extension.startActivityWithFinish

class SplashActivity : BaseActivity() {

    private val binding by contentView<SplashActivity, ActivitySplashBinding>(R.layout.activity_splash)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this

        startApp()
    }

    private fun startApp() = Handler().postDelayed({
        when {
            !permissionManager.hasRequiredPermissions() -> permissionManager.requestAllAppPermissions()
            connectivityManager.isOffline() -> connectivityManager.handleOffline()
            else -> openDashboard()
        }
    }, Values.SPLASH_TIME_IN_MILLIS)

    private fun openDashboard() = startActivityWithFinish(DashboardActivity.intent(this))

    companion object {
        fun intent(context: Context) = Intent(context, SplashActivity::class.java)
    }
}
