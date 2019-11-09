package pl.mihau.moviedb.common.view

import androidx.fragment.app.Fragment
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import io.reactivex.disposables.CompositeDisposable
import pl.mihau.moviedb.util.list.item.progress.ProgressHorizontalListItem

abstract class BaseFragment<T : BaseActivity> : Fragment() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    fun setProgressVisibility (show: Boolean, progressItem: GenericItem = ProgressHorizontalListItem(), footerAdapter: GenericItemAdapter) {
        if (show) footerAdapter.add(progressItem)
        else footerAdapter.clear()
    }

    @Suppress("UNCHECKED_CAST")
    fun parentActivity(): T = requireActivity() as T
}
