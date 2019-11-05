package pl.mihau.moviedb.util.application

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.ContextThemeWrapper
import pl.mihau.moviedb.R
import pl.mihau.moviedb.common.view.BaseActivity

class ConnectivityManager(private val activity: BaseActivity) {

    fun isOffline(): Boolean {
        var result = false
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = true
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = true
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return !result
    }

    fun handleOffline() {
        AlertDialog.Builder(ContextThemeWrapper(activity, R.style.StyledAlertDialog))
            .setTitle(R.string.offline_title)
            .setMessage(R.string.offline_message)
            .setPositiveButton(R.string.offline_button_exit) { _, _ -> activity.processManager.exit() }
            .setNegativeButton(R.string.offline_button_restart) { _, _ -> activity.processManager.relaunchFromStartup()}
            .setCancelable(false)
            .show()
    }
}