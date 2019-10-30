package pl.mihau.moviedb.common.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import pl.mihau.moviedb.util.application.ProcessManager
import pl.mihau.moviedb.util.permission.PermissionManager

abstract class BaseFragment<T : BaseActivity> : Fragment() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!parentActivity().permissionManager.hasRequiredPermissions()) {
            parentActivity().processManager.restartProcess()
            return null
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    open fun onBackPressedHandled(): Boolean = false

    @Suppress("UNCHECKED_CAST")
    fun parentActivity(): T = requireActivity() as T
}
