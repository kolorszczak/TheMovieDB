package pl.mihau.moviedb.search.ui.item

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import pl.mihau.moviedb.R
import pl.mihau.moviedb.databinding.ItemMovieSmallBinding
import pl.mihau.moviedb.list.model.Movie

open class MovieSmallListItem(var movie: Movie) : AbstractItem<MovieSmallListItem.ViewHolder>() {

    override val type = R.id.item_movie_small

    override val layoutRes = R.layout.item_movie_small

    override fun getViewHolder(v: View) = ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<MovieSmallListItem>(view) {
        var binding: ItemMovieSmallBinding = ItemMovieSmallBinding.bind(view)

        override fun bindView(item: MovieSmallListItem, payloads: MutableList<Any>) {
            binding.movie = item.movie
        }

        override fun unbindView(item: MovieSmallListItem) {
            binding.movie = null
        }
    }
}