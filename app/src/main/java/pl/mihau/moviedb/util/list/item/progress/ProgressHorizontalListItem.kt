package pl.mihau.moviedb.util.list.item.progress

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import pl.mihau.moviedb.R
import pl.mihau.moviedb.databinding.ItemProgressHorizontalBinding
import pl.mihau.moviedb.util.extension.setVisibility

class ProgressHorizontalListItem : AbstractItem<ProgressHorizontalListItem.ViewHolder>() {

    override val type = R.id.item_progress

    override val layoutRes = R.layout.item_progress_horizontal

    override fun getViewHolder(v: View) =
        ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ProgressHorizontalListItem>(view) {
        var binding: ItemProgressHorizontalBinding = ItemProgressHorizontalBinding.bind(view)

        override fun bindView(item: ProgressHorizontalListItem, payloads: MutableList<Any>) {
            binding.progressBar.setVisibility(true)
        }

        override fun unbindView(item: ProgressHorizontalListItem) {
            binding.progressBar.setVisibility(false)
        }
    }
}