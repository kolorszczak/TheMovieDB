package pl.mihau.moviedb.util.lifecycle

import android.app.Activity
import java.util.concurrent.atomic.AtomicInteger

class AppVisibilityResolver : ActivityLifecycleObservable {

    private val activitiesCount = AtomicInteger()

    private var appVisibility: AppVisibility = AppVisibility.BACKGROUND

    override fun onActivityStarted(activity: Activity) {
        onCountChanged(activitiesCount.getAndIncrement())
    }

    override fun onActivityStopped(activity: Activity) {
        onCountChanged(activitiesCount.getAndDecrement())
    }

    private fun onCountChanged(lastActivitiesCount: Int) {
        if (activitiesCount.get() == 0) {
            notifyAppInBackground()
        } else if (firstVisibleActivity(lastActivitiesCount)) {
            notifyAppInForeground()
        }
    }

    private fun firstVisibleActivity(lastActivitiesCount: Int): Boolean {
        return activitiesCount.get() >= 1 && lastActivitiesCount == 0
    }

    private fun notifyAppInForeground() {
        appVisibility = AppVisibility.FOREGROUND
    }

    private fun notifyAppInBackground() {
        appVisibility = AppVisibility.BACKGROUND
    }

    enum class AppVisibility {
        FOREGROUND,
        BACKGROUND
    }
}
