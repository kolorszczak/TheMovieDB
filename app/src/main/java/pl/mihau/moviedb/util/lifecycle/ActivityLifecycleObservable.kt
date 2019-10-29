package pl.mihau.moviedb.util.lifecycle

import android.app.Activity
import android.os.Bundle

interface ActivityLifecycleObservable {

    fun onActivityCreated(activity: Activity) = Unit

    fun onActivityStarted(activity: Activity) = Unit

    fun onActivityResumed(activity: Activity) = Unit

    fun onActivityPaused(activity: Activity) = Unit

    fun onActivityStopped(activity: Activity) = Unit

    fun onActivityDestroyed(activity: Activity) = Unit

    fun onActivitySaveInstance(activity: Activity, outState: Bundle) = Unit
}
