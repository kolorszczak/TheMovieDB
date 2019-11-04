package pl.mihau.moviedb.util.list.item

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import pl.mihau.moviedb.R
import pl.mihau.moviedb.databinding.ItemProgressBinding

open class ProgressListItem : AbstractItem<ProgressListItem.ViewHolder>() {

    override val type = R.id.item_progress

    override val layoutRes = R.layout.item_progress

    override fun getViewHolder(v: View) = ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ProgressListItem>(view) {
        var binding: ItemProgressBinding = ItemProgressBinding.bind(view)

        override fun bindView(item: ProgressListItem, payloads: MutableList<Any>) {
            binding.progressBar.visibility = View.VISIBLE
        }

        override fun unbindView(item: ProgressListItem) {
            binding.progressBar.visibility = View.GONE
        }
    }
}

