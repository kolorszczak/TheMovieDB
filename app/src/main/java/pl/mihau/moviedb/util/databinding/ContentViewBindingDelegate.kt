package pl.mihau.moviedb.util.databinding

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

class ContentViewBindingDelegate<in R : Activity, out T : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) {

    private var binding: T? = null

    operator fun getValue(activity: R, property: KProperty<*>): T {
        if (binding == null) {
            binding = DataBindingUtil.setContentView(activity, layoutRes)
        }
        return binding!!
    }
}

fun <R : Activity, T : ViewDataBinding> contentView(
    @LayoutRes layoutRes: Int
): ContentViewBindingDelegate<R, T> = ContentViewBindingDelegate(layoutRes)


class ContentViewFragmentBindingDelegate<in R : Fragment, out T : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) {

    private var binding: T? = null

    operator fun getValue(fragment: R, property: KProperty<*>): T {
        if (binding == null) {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(fragment.context),
                layoutRes,
                fragment.view?.parent?.let { it as ViewGroup },
                false)
        }
        return binding!!
    }
}

fun <R : Fragment, T : ViewDataBinding> inflate(
    @LayoutRes layoutRes: Int
): ContentViewFragmentBindingDelegate<R, T> = ContentViewFragmentBindingDelegate(layoutRes)
