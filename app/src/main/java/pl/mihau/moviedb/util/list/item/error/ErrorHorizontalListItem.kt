package pl.mihau.moviedb.util.list.item.error

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import pl.mihau.moviedb.R
import pl.mihau.moviedb.databinding.ItemErrorHorizontalBinding

class ErrorHorizontalListItem : AbstractItem<ErrorHorizontalListItem.ViewHolder>() {

    override val type = R.id.item_error

    override val layoutRes = R.layout.item_error_horizontal

    override fun getViewHolder(v: View) =
        ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ErrorHorizontalListItem>(view) {
        var binding: ItemErrorHorizontalBinding = ItemErrorHorizontalBinding.bind(view)

        override fun bindView(item: ErrorHorizontalListItem, payloads: MutableList<Any>) = Unit

        override fun unbindView(item: ErrorHorizontalListItem) = Unit
    }
}