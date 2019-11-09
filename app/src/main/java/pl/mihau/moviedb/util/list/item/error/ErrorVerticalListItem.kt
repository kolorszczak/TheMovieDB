package pl.mihau.moviedb.util.list.item.error

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import pl.mihau.moviedb.R
import pl.mihau.moviedb.databinding.ItemErrorBinding

class ErrorVerticalListItem : AbstractItem<ErrorVerticalListItem.ViewHolder>() {

    override val type = R.id.item_error

    override val layoutRes = R.layout.item_error

    override fun getViewHolder(v: View) =
        ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ErrorVerticalListItem>(view) {
        var binding: ItemErrorBinding = ItemErrorBinding.bind(view)

        override fun bindView(item: ErrorVerticalListItem, payloads: MutableList<Any>) = Unit

        override fun unbindView(item: ErrorVerticalListItem) = Unit
    }
}

