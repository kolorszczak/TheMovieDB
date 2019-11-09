package pl.mihau.moviedb.util.application

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.ContextThemeWrapper
import pl.mihau.moviedb.R
import pl.mihau.moviedb.common.view.BaseActivity

class DialogManager(private val activity: BaseActivity) {

    private lateinit var dialog: AlertDialog

    fun handleError(errorMsg: String?, retryFunction: () -> Unit) {
        if (shouldShowNewDialog()) {
            dialog = AlertDialog.Builder(ContextThemeWrapper(activity, R.style.StyledAlertDialog))
                .setTitle(R.string.error_title)
                .setMessage(errorMsg ?: activity.getString(R.string.error_message))
                .setPositiveButton(R.string.retry) { _, _ -> retryFunction() }
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    fun handleError(errorMsg: String? = null, buttonText: String? = null, buttonAction: (() -> Unit)? = null) {
        if (shouldShowNewDialog()) {
            AlertDialog.Builder(ContextThemeWrapper(activity, R.style.StyledAlertDialog))
                .apply {
                    val function: (DialogInterface, Int) -> Unit = { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        buttonAction?.invoke()
                    }

                    setTitle(R.string.error_title)
                    setMessage(errorMsg ?: activity.getString(R.string.error_message))
                    setPositiveButton(buttonText ?: activity.getText(R.string.retry), function)
            }.show()
        }
    }

    private fun shouldShowNewDialog() = !::dialog.isInitialized || !dialog.isShowing
}