package pl.mihau.moviedb.list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.mihau.moviedb.R
import pl.mihau.moviedb.common.view.BaseFragment
import pl.mihau.moviedb.dashboard.view.DashboardActivity
import pl.mihau.moviedb.databinding.FragmentMovieListBinding
import pl.mihau.moviedb.list.model.MovieListType
import pl.mihau.moviedb.util.databinding.inflate
import pl.mihau.moviedb.util.extension.inTransaction

class MovieListFragment : BaseFragment<DashboardActivity>() {

    private val binding by inflate<MovieListFragment, FragmentMovieListBinding>(R.layout.fragment_movie_list)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this

        childFragmentManager.inTransaction {
            add(R.id.container, ListFragment.instance(MovieListType.NOW_PLAYING))
            add(R.id.container, ListFragment.instance(MovieListType.UPCOMING))
            add(R.id.container, ListFragment.instance(MovieListType.POPULAR))
        }
    }
}