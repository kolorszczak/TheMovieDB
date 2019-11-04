package pl.mihau.moviedb.list.view

import android.app.Activity
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
import kotlinx.android.synthetic.main.fragment_movie_list.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.mihau.moviedb.R
import pl.mihau.moviedb.common.Codes
import pl.mihau.moviedb.common.view.BaseFragment
import pl.mihau.moviedb.dashboard.view.DashboardActivity
import pl.mihau.moviedb.databinding.FragmentMovieListBinding
import pl.mihau.moviedb.details.view.MovieDetailsActivity
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.list.model.MovieListType
import pl.mihau.moviedb.list.model.MovieListType.*
import pl.mihau.moviedb.list.ui.item.MovieListItem
import pl.mihau.moviedb.list.viewmodel.MovieListViewModel
import pl.mihau.moviedb.util.application.FavoritesManager
import pl.mihau.moviedb.util.databinding.inflate
import pl.mihau.moviedb.util.extension.fastAdapter
import pl.mihau.moviedb.util.extension.setVisibility
import pl.mihau.moviedb.util.list.HorizontalSpaceItemDecoration
import pl.mihau.moviedb.util.list.item.ProgressHorizontalListItem

class MovieListFragment : BaseFragment<DashboardActivity>() {

    private val binding by inflate<MovieListFragment, FragmentMovieListBinding>(R.layout.fragment_movie_list)

    private val viewModel by viewModel<MovieListViewModel>()

    private val nowPlayingFooterAdapter: ItemAdapter<ProgressHorizontalListItem> = ItemAdapter()
    private val nowPlayingItemAdapter: ItemAdapter<MovieListItem> = ItemAdapter()
    private val nowPlayingAdapter: GenericFastAdapter =
        fastAdapter(nowPlayingItemAdapter, nowPlayingFooterAdapter)

    private val upcomingFooterAdapter: ItemAdapter<ProgressHorizontalListItem> = ItemAdapter()
    private val upcomingItemAdapter: ItemAdapter<MovieListItem> = ItemAdapter()
    private val upcomingAdapter: GenericFastAdapter =
        fastAdapter(upcomingItemAdapter, upcomingFooterAdapter)

    private val popularFooterAdapter: ItemAdapter<ProgressHorizontalListItem> = ItemAdapter()
    private val popularItemAdapter: ItemAdapter<MovieListItem> = ItemAdapter()
    private val popularAdapter: GenericFastAdapter =
        fastAdapter(popularItemAdapter, popularFooterAdapter)

    private val adapters = listOf(nowPlayingAdapter, upcomingAdapter, popularAdapter)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.also {
            it.lifecycleOwner = this
        }

        viewModel.state.observe(this, Observer {
            when (it) {
                is MovieListViewModel.MovieState.Loading -> it.listTypes.forEach { listType -> setProgressVisibility(listType, true) }
                is MovieListViewModel.MovieState.PageLoaded -> setupList(it.listType, it.data)
                is MovieListViewModel.MovieState.DataLoaded -> {
                    setupList(NOW_PLAYING, it.data.nowPlaying.results)
                    scrollToTopOf(NOW_PLAYING)

                    setupList(UPCOMING, it.data.upcoming.results)
                    scrollToTopOf(UPCOMING)

                    setupList(POPULAR, it.data.popular.results)
                    scrollToTopOf(POPULAR)
                }
                is MovieListViewModel.MovieState.Error -> values().forEach { listType -> handleError(listType) }
            }
        })

        setupRecyclerViews()
        setupPaging()
        setupAdapters()
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Codes.MOVIE_DETAILS) {
            adapters.forEach { it.notifyAdapterDataSetChanged() }
        }
    }

    private fun init() {
        viewModel.invokeAction(MovieListViewModel.MovieEvent.Action.Init)
    }

    private fun setupList(listType: MovieListType, data: List<Movie>) {
        val favoritesManager = get<FavoritesManager>()

        when (listType) {
            NOW_PLAYING -> nowPlayingItemAdapter
            UPCOMING -> upcomingItemAdapter
            POPULAR -> popularItemAdapter
        }.apply {
            data.forEach { add(MovieListItem(it, favoritesManager)) }
        }

        setupSectionViews(listType, true)
        setProgressVisibility(listType, false)
    }

    private fun handleError(listType: MovieListType) {
        setProgressVisibility(listType, false)
        when (listType) {
            NOW_PLAYING -> setupSectionViews(listType, nowPlayingItemAdapter.adapterItemCount == 0)
            UPCOMING -> setupSectionViews(listType, upcomingItemAdapter.adapterItemCount == 0)
            POPULAR -> setupSectionViews(listType, popularItemAdapter.adapterItemCount == 0)
        }
    }

    private fun setupSectionViews(listType: MovieListType, show: Boolean) {
        when (listType) {
            NOW_PLAYING -> nowPlaying.setVisibility(show)
            UPCOMING -> upcoming.setVisibility(show)
            POPULAR -> popular.setVisibility(show)
        }
    }

    private fun scrollToTopOf(listType: MovieListType) {
        when (listType) {
            NOW_PLAYING -> nowPlayingList.smoothScrollToPosition(0)
            UPCOMING -> upcomingList.smoothScrollToPosition(0)
            POPULAR -> popularList.smoothScrollToPosition(0)
        }
    }

    private fun setProgressVisibility(listType: MovieListType, show: Boolean) {
        when (listType) {
            NOW_PLAYING -> {
                if (show) nowPlayingFooterAdapter.add(ProgressHorizontalListItem())
                else nowPlayingFooterAdapter.clear()
            }
            UPCOMING -> {
                if (show) upcomingFooterAdapter.add(ProgressHorizontalListItem())
                else upcomingFooterAdapter.clear()
            }
            POPULAR -> {
                if (show) popularFooterAdapter.add(ProgressHorizontalListItem())
                else popularFooterAdapter.clear()
            }
        }
    }

    private fun setupRecyclerViews() {
        nowPlayingList.adapter = nowPlayingAdapter
        upcomingList.adapter = upcomingAdapter
        popularList.adapter = popularAdapter

        listOf(nowPlayingList, upcomingList, popularList).forEach {
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.addItemDecoration(HorizontalSpaceItemDecoration(R.dimen.margin32))
        }
    }

    private fun setupPaging() {
        nowPlayingList.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore(currentPage: Int) {
                with(viewModel) {
                    if (!isLoadingNowPlaying && currentPage < totalNowPlayingPage) {
                        invokeAction(MovieListViewModel.MovieEvent.Action.LoadMore(NOW_PLAYING))
                    }
                }
            }
        })

        upcomingList.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore(currentPage: Int) {
                with(viewModel) {
                    if (!isLoadingUpcoming && currentPage < totalUpcomingPage) {
                        invokeAction(MovieListViewModel.MovieEvent.Action.LoadMore(UPCOMING))
                    }
                }
            }
        })

        popularList.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore(currentPage: Int) {
                with(viewModel) {
                    if (!isLoadingPopular && currentPage < totalPopularPage) {
                        invokeAction(MovieListViewModel.MovieEvent.Action.LoadMore(POPULAR))
                    }
                }
            }
        })
    }

    private fun setupAdapters() {
        listOf(nowPlayingAdapter, upcomingAdapter, popularAdapter).forEach { adapter ->
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
                override fun onBind(viewHolder: RecyclerView.ViewHolder): View? = (viewHolder as? MovieListItem.ViewHolder)?.run { this.binding.favoritesImageView }

                override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<GenericItem>, item: GenericItem) {
                    when (item) {
                        is MovieListItem -> toggleFavorite(item)
                    }
                }
            })
        }
    }

    private fun toggleFavorite(movie: MovieListItem) {
        get<FavoritesManager>().toggleFavorite(movie.movie.id)
        adapters.forEach { it.notifyAdapterDataSetChanged() }
    }
}