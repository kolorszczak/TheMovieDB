package pl.mihau.moviedb.common.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import pl.mihau.moviedb.util.application.ProcessManager
import pl.mihau.moviedb.util.permission.PermissionManager

abstract class BaseActivity : AppCompatActivity() {

    protected val compositeDisposable = CompositeDisposable()

    val processManager: ProcessManager by inject { parametersOf(this) }

    val permissionManager: PermissionManager by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!permissionManager.hasRequiredPermissions()) {
            processManager.restartProcess()
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
