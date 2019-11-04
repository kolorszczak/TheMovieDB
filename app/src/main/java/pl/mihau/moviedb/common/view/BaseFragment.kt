package pl.mihau.moviedb.common.view

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<T : BaseActivity> : Fragment() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    @Suppress("UNCHECKED_CAST")
    fun parentActivity(): T = requireActivity() as T
}
