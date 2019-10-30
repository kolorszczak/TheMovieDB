package pl.mihau.moviedb.util.application

import android.content.Intent
import com.jakewharton.processphoenix.ProcessPhoenix
import pl.mihau.moviedb.common.view.BaseActivity
import pl.mihau.moviedb.splash.view.SplashActivity
import pl.mihau.moviedb.util.extension.requireContext
import timber.log.Timber

class ProcessManager(private val activity: BaseActivity) {

    fun restartProcess() {
        Timber.i("Restarting process through ProcessManager.")
        ProcessPhoenix.triggerRebirth(activity.requireContext())
    }

    fun relaunchFromStartup() {
        SplashActivity.intent(activity)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            .also { activity.startActivity(it) }
    }
}
