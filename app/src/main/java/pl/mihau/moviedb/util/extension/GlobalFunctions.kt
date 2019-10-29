package pl.mihau.moviedb.util.extension

import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.IAdapter

fun <Item : GenericItem> fastAdapter(vararg adapter: IAdapter<out Item>) =
    FastAdapter.with<Item, IAdapter<out Item>>(adapter.toList())