package pl.mihau.moviedb.list.ui.item
import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import pl.mihau.moviedb.R
import pl.mihau.moviedb.databinding.ItemMovieBinding
import pl.mihau.moviedb.list.model.Movie

class MovieListItem(var movie: Movie, var isFavorite: Boolean) : AbstractItem<MovieListItem.ViewHolder>() {

    override val type = R.id.item_movie

    override val layoutRes = R.layout.item_movie

    override fun getViewHolder(v: View) = ViewHolder(v)

    override var identifier = movie.id.toLong()

    class ViewHolder(view: View) : FastAdapter.ViewHolder<MovieListItem>(view) {
        var binding: ItemMovieBinding = ItemMovieBinding.bind(view)

        override fun bindView(item: MovieListItem, payloads: MutableList<Any>) {
            binding.movie = item.movie
            binding.isFavorite = item.isFavorite
        }

        override fun unbindView(item: MovieListItem) {
            binding.movie = null
            binding.isFavorite = null
        }
    }
}