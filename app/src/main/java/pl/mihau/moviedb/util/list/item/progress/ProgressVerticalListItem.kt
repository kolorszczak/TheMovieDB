package pl.mihau.moviedb.util.list.item.progress

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import pl.mihau.moviedb.R
import pl.mihau.moviedb.databinding.ItemProgressBinding
import pl.mihau.moviedb.util.extension.setVisibility

class ProgressVerticalListItem : AbstractItem<ProgressVerticalListItem.ViewHolder>() {

    override val type = R.id.item_progress

    override val layoutRes = R.layout.item_progress

    override fun getViewHolder(v: View) =
        ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ProgressVerticalListItem>(view) {
        var binding: ItemProgressBinding = ItemProgressBinding.bind(view)

        override fun bindView(item: ProgressVerticalListItem, payloads: MutableList<Any>) {
            binding.progressBar.setVisibility(true)
        }

        override fun unbindView(item: ProgressVerticalListItem) {
            binding.progressBar.setVisibility(false)
        }
    }
}

