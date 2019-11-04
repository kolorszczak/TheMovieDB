package pl.mihau.moviedb.list.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.mihau.moviedb.R
import pl.mihau.moviedb.api.ListResponse
import pl.mihau.moviedb.common.Codes
import pl.mihau.moviedb.common.Keys
import pl.mihau.moviedb.common.view.BaseFragment
import pl.mihau.moviedb.dashboard.view.DashboardActivity
import pl.mihau.moviedb.databinding.FragmentListBinding
import pl.mihau.moviedb.details.view.MovieDetailsActivity
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.list.model.MovieListType
import pl.mihau.moviedb.list.ui.item.MovieListItem
import pl.mihau.moviedb.list.viewmodel.MovieListViewModel
import pl.mihau.moviedb.util.application.FavoritesManager
import pl.mihau.moviedb.util.databinding.inflate
import pl.mihau.moviedb.util.extension.fastAdapter
import pl.mihau.moviedb.util.extension.requiredParcelable
import pl.mihau.moviedb.util.extension.setVisibility
import pl.mihau.moviedb.util.list.HorizontalSpaceItemDecoration
import pl.mihau.moviedb.util.list.item.ProgressHorizontalListItem

class ListFragment : BaseFragment<DashboardActivity>() {

    private val binding by inflate<ListFragment, FragmentListBinding>(R.layout.fragment_list)

    private val viewModel by viewModel<MovieListViewModel>()

    private val footerAdapter: ItemAdapter<ProgressHorizontalListItem> = ItemAdapter()
    private val itemAdapter: ItemAdapter<MovieListItem> = ItemAdapter()
    private val adapter: GenericFastAdapter = fastAdapter(itemAdapter, footerAdapter)

    private val listType by lazy { arguments.requiredParcelable<MovieListType>(Keys.LIST_TYPE) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.also {
            it.lifecycleOwner = this
            it.titleRes = listType.screenTitleRes
        }

        viewModel.state.observe(this, Observer {
            when (it) {
                is MovieListViewModel.MovieState.Loading -> setProgressVisibility(true)
                is MovieListViewModel.MovieState.DataLoaded -> setupList(it.data)
                is MovieListViewModel.MovieState.Error -> handleError()
            }
        })

        setupRecyclerViews()
        setupPaging()
        setupAdapters()
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Codes.MOVIE_DETAILS) {
            adapter.notifyAdapterDataSetChanged()
        }
    }

    private fun init() {
        viewModel.listType = listType
        viewModel.invokeAction(MovieListViewModel.MovieEvent.Action.Init)
    }

    private fun setupList(data: ListResponse<Movie>) {
        val favoritesManager = get<FavoritesManager>()

        itemAdapter.add(data.results.map { movie -> MovieListItem(movie, favoritesManager)})
        if (data.page == 1) scrollToTopOf()


        setupSectionViews(true)
        setProgressVisibility(false)
    }

    private fun handleError() {
        setProgressVisibility(false)
        setupSectionViews(itemAdapter.adapterItemCount == 0)
        error("list error")
    }

    private fun setupSectionViews(show: Boolean) {
        title.setVisibility(show)
        list.setVisibility(show)
    }

    private fun scrollToTopOf() = list.smoothScrollToPosition(0)

    private fun setProgressVisibility(show: Boolean) {
        if (show) footerAdapter.add(ProgressHorizontalListItem())
        else footerAdapter.clear()
    }

    private fun setupRecyclerViews() {
        list.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.addItemDecoration(HorizontalSpaceItemDecoration(R.dimen.margin32))
        }
    }

    private fun setupPaging() {
        list.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore(currentPage: Int) {
                with(viewModel) {
                    val isLoading = state.value is MovieListViewModel.MovieState.Loading

                    var nextPage = 1
                    var isCurrentlyDataLoaded = false
                    var canLoadMorePages = false

                    if (state.value is MovieListViewModel.MovieState.DataLoaded) {
                        with(state.value as MovieListViewModel.MovieState.DataLoaded) {
                            nextPage = data.page + 1
                            isCurrentlyDataLoaded = true
                            canLoadMorePages = nextPage <= data.totalPages
                        }
                    }

                    if (!isLoading && (isCurrentlyDataLoaded && canLoadMorePages)) {
                        invokeAction(MovieListViewModel.MovieEvent.Action.LoadMore(nextPage))
                    }
                }
            }
        })
    }

    private fun setupAdapters() {
        adapter.onClickListener = { _, _, item, _ ->
            when (item) {
                is MovieListItem -> {
                    startActivityForResult(MovieDetailsActivity.intent(requireContext(), item.movie), Codes.MOVIE_DETAILS)
                    true
                }
                else -> false
            }
        }

        adapter.addEventHook(object : ClickEventHook<GenericItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? =
                (viewHolder as? MovieListItem.ViewHolder)?.run { this.binding.favoritesImageView }

            override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<GenericItem>, item: GenericItem) {
                if (item is MovieListItem) {
                    toggleFavorite(item)
                }
            }
        })
    }

    private fun toggleFavorite(movie: MovieListItem) {
        get<FavoritesManager>().toggleFavorite(movie.movie.id)
        adapter.notifyAdapterDataSetChanged()
    }

    companion object {

        fun instance(listType: MovieListType) = ListFragment().apply {  arguments = Bundle().apply { putParcelable(Keys.LIST_TYPE, listType) } }
    }
}