package pl.mihau.moviedb.util.databinding

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import pl.mihau.moviedb.R
import pl.mihau.moviedb.common.Strings
import pl.mihau.moviedb.common.Values

@BindingAdapter(value = ["label"], requireAll = false)
fun AppCompatTextView.bindLabel(any: Any?) {
    text = when (any) {
        is String -> any
        null -> Strings.empty
        else -> any.toString()
    }
}

@BindingAdapter("image")
fun AppCompatImageView.bindImage(imageUrl: String?) {
    Glide.with(context)
        .load(
            if (imageUrl != null) Values.IMAGES_PREFIX + imageUrl.substring(1)
            else R.drawable.im_empty_poster
        )
        .into(this)
}