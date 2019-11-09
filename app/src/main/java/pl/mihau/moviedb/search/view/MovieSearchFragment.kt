package pl.mihau.moviedb.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.fragment_movie_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.mihau.moviedb.R
import pl.mihau.moviedb.common.Strings.empty
import pl.mihau.moviedb.common.view.BaseFragment
import pl.mihau.moviedb.dashboard.view.DashboardActivity
import pl.mihau.moviedb.databinding.FragmentMovieSearchBinding
import pl.mihau.moviedb.details.view.MovieDetailsActivity
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.search.ui.item.MovieSmallListItem
import pl.mihau.moviedb.search.viewmodel.MovieSearchViewModel
import pl.mihau.moviedb.util.databinding.inflate
import pl.mihau.moviedb.util.extension.fastAdapter
import pl.mihau.moviedb.util.extension.schedule
import pl.mihau.moviedb.util.list.RxSearchObservable
import pl.mihau.moviedb.util.list.VerticalSpaceItemDecoration
import pl.mihau.moviedb.util.list.item.error.ErrorVerticalListItem
import pl.mihau.moviedb.util.list.item.progress.ProgressVerticalListItem
import java.util.concurrent.TimeUnit

class MovieSearchFragment : BaseFragment<DashboardActivity>() {

    private val binding by inflate<MovieSearchFragment, FragmentMovieSearchBinding>(R.layout.fragment_movie_search)

    private val viewModel by viewModel<MovieSearchViewModel>()

    private val footerAdapter: GenericItemAdapter = ItemAdapter()
    private val itemAdapter: GenericItemAdapter = ItemAdapter()
    private val adapter: GenericFastAdapter = fastAdapter(itemAdapter, footerAdapter)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this

        viewModel.state.observe(this, Observer {
            when (it) {
                MovieSearchViewModel.SearchState.Empty -> clear()
                MovieSearchViewModel.SearchState.Loading -> setProgressVisibility(true, ProgressVerticalListItem(), footerAdapter)
                is MovieSearchViewModel.SearchState.DataLoaded -> setupList(it.data)
                is MovieSearchViewModel.SearchState.Error -> handleError(it.throwable)
            }
        })

        init()
    }

    private fun init() {
        setupRecyclerViews()
        setupAdapter()
        setupSearchView()
    }

    private fun setupList(data: List<Movie>) {
        setProgressVisibility(false, footerAdapter = footerAdapter)
        data.forEach { movie -> itemAdapter.add(MovieSmallListItem(movie)) }
    }

    private fun setupSearchView() {
        searchView.apply {
            RxSearchObservable.fromView(this)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter { text -> text.isNotEmpty() }
                .filter { s -> (viewModel.state.value as? MovieSearchViewModel.SearchState.DataLoaded)?.keyword != s }
                .distinctUntilChanged()
                .schedule(parentActivity().schedulerProvider)
                .subscribe { s -> query(s) }
                .let { compositeDisposable.add(it) }

            findViewById<View>(R.id.search_close_btn).setOnClickListener {
                setQuery(empty, false)
                viewModel.invokeAction(MovieSearchViewModel.SearchEvent.Action.Clear)
            }
        }
    }

    private fun setupRecyclerViews() {
        recyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            it.addItemDecoration(VerticalSpaceItemDecoration(R.dimen.margin4))
            it.addOnScrollListener(object : EndlessRecyclerOnScrollListener(footerAdapter) {
                override fun onLoadMore(currentPage: Int) {
                    with(viewModel) {
                        val isLoading = state.value is MovieSearchViewModel.SearchState.Loading

                        var isCurrentlyDataLoaded = false
                        var canLoadMorePages = false

                        if (state.value is MovieSearchViewModel.SearchState.DataLoaded) {
                            with(state.value as MovieSearchViewModel.SearchState.DataLoaded) {
                                val nextPage = page + 1
                                isCurrentlyDataLoaded = true
                                canLoadMorePages = nextPage <= totalPages
                            }
                        }

                        if (!isLoading && (isCurrentlyDataLoaded && canLoadMorePages)) {
                            invokeAction(MovieSearchViewModel.SearchEvent.Action.LoadNewPage)
                        }
                    }
                }
            })
        }
    }

    private fun setupAdapter() {
        adapter.onClickListener = { _, _, item, _ ->
            when (item) {
                is MovieSmallListItem -> startActivity(MovieDetailsActivity.intent(requireContext(), item.movie))
            }
            true
        }
    }

    private fun query(query: String) {
        itemAdapter.clear()
        viewModel.invokeAction(MovieSearchViewModel.SearchEvent.Action.Query(query))
    }

    private fun clear() {
        footerAdapter.clear()
        itemAdapter.clear()
    }

    private fun handleError(throwable: Throwable) {
        setProgressVisibility(false, footerAdapter = footerAdapter)
        itemAdapter.clear()
        itemAdapter.add(ErrorVerticalListItem())
        parentActivity().dialogManager.handleError(throwable.message)
    }
}