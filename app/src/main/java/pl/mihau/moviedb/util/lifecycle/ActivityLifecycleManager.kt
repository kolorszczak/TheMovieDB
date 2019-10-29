package pl.mihau.moviedb.util.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ActivityLifecycleManager(private val activityLifecycleObservable: ActivityLifecycleObservable) :
    Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityLifecycleObservable.onActivityCreated(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        activityLifecycleObservable.onActivityStarted(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        activityLifecycleObservable.onActivityResumed(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        activityLifecycleObservable.onActivityPaused(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        activityLifecycleObservable.onActivityStopped(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityLifecycleObservable.onActivityDestroyed(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        activityLifecycleObservable.onActivitySaveInstance(activity, outState)
    }
}
