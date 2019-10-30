package pl.mihau.moviedb.splash.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.mihau.moviedb.R
import pl.mihau.moviedb.common.view.BaseActivity
import pl.mihau.moviedb.dashboard.view.DashboardActivity
import pl.mihau.moviedb.databinding.ActivitySplashBinding
import pl.mihau.moviedb.splash.viewmodel.SplashViewModel
import pl.mihau.moviedb.util.databinding.contentView
import pl.mihau.moviedb.util.extension.startActivityWithFinish

class SplashActivity : BaseActivity() {

    private val binding by contentView<SplashActivity, ActivitySplashBinding>(R.layout.activity_splash)

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        viewModel.state.observe(this, Observer {
            when (it) {
                SplashViewModel.SplashState.Dashboard -> openDashboard()
                SplashViewModel.SplashState.Error -> processManager.relaunchFromStartup()
            }
        })

        checkPermissions()
    }

    private fun openDashboard() = startActivityWithFinish(DashboardActivity.intent(this))

    private fun checkPermissions() {
        when {
            permissionManager.hasRequiredPermissions() -> {
                permissionManager.requestAllAppPermissions()
                    .map {
                        when {
                            it.granted -> SplashViewModel.SplashEvent.PermissionGranted
                            else -> SplashViewModel.SplashEvent.PermissionNotGranted(it.shouldShowRequestPermissionRationale)
                        }
                    }
                    .subscribe { viewModel.invokeAction(it) }
                    .let { compositeDisposable.add(it) }
            }
            else -> viewModel.invokeAction(SplashViewModel.SplashEvent.PermissionGranted)
        }
    }

    companion object {
        fun intent(context: Context) = Intent(context, SplashActivity::class.java)
    }
}
