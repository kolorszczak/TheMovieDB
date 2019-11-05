package pl.mihau.moviedb.common.view

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import pl.mihau.moviedb.util.application.ConnectivityManager
import pl.mihau.moviedb.util.application.ProcessManager
import pl.mihau.moviedb.util.permission.PermissionManager

abstract class BaseActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    val processManager: ProcessManager by inject { parametersOf(this) }

    val permissionManager: PermissionManager by inject { parametersOf(this) }

    val connectivityManager: ConnectivityManager by inject { parametersOf(this) }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
