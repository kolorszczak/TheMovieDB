package pl.mihau.moviedb.search.ui.item

import android.view.View
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import pl.mihau.moviedb.R
import pl.mihau.moviedb.common.Values
import pl.mihau.moviedb.databinding.ItemMovieSmallBinding
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.util.extension.setVisibility

open class MovieSmallListItem(var movie: Movie) : AbstractItem<MovieSmallListItem.ViewHolder>() {

    override val type = R.id.item_movie_small

    override val layoutRes = R.layout.item_movie_small

    override fun getViewHolder(v: View) = ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<MovieSmallListItem>(view) {
        var binding: ItemMovieSmallBinding = ItemMovieSmallBinding.bind(view)

        override fun bindView(item: MovieSmallListItem, payloads: MutableList<Any>) {
            with(binding) {

                Glide.with(itemView)
                    .load(
                        if (item.movie.posterPath != null) Values.IMAGES_PREFIX + item.movie.posterPath?.substring(1)
                        else R.drawable.im_empty_poster)
                    .into(posterImageView)
                titleTextView.text = item.movie.title
                item.movie.overview?.let { descriptionTextView.text = it } ?: descriptionTextView.setVisibility(false)
            }
        }

        override fun unbindView(item: MovieSmallListItem) {
            with(binding) {
                posterImageView.setImageBitmap(null)
                titleTextView.text = null
                descriptionTextView.text = null
            }
        }
    }
}