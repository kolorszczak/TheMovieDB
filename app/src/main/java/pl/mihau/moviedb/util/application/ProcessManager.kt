package pl.mihau.moviedb.util.application

import android.content.Intent
import pl.mihau.moviedb.common.view.BaseActivity
import pl.mihau.moviedb.splash.view.SplashActivity
import kotlin.system.exitProcess

class ProcessManager(private val activity: BaseActivity) {

    fun exit() {
        exitProcess(-1)
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
