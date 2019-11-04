package pl.mihau.moviedb.list.viewmodel

import io.reactivex.Single
import io.reactivex.functions.Function3
import kotlinx.android.parcel.Parcelize
import pl.mihau.moviedb.api.ListResponse
import pl.mihau.moviedb.api.MovieDBRepository
import pl.mihau.moviedb.list.model.DashboardContent
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.list.model.MovieListType
import pl.mihau.moviedb.list.model.MovieListType.*
import pl.mihau.moviedb.util.state.Event
import pl.mihau.moviedb.util.state.SideEffect
import pl.mihau.moviedb.util.state.State
import pl.mihau.moviedb.util.state.StatefulViewModel

class MovieListViewModel(private val movieDBRepository: MovieDBRepository) : StatefulViewModel<MovieListViewModel.MovieState, MovieListViewModel.MovieEvent>(MovieState.Empty) {

    sealed class MovieState : State {
        @Parcelize object Empty : MovieState()
        @Parcelize object Loading : MovieState()
        @Parcelize data class DataLoaded(val data: ListResponse<Movie>) : MovieState()
        @Parcelize object Error : MovieState()
    }

    sealed class MovieEvent : Event {
        object InitFailure : MovieEvent()
        data class InitSuccess(val data: DashboardContent) : MovieEvent()
        data class LoadingFailure(val listType: MovieListType) : MovieEvent()
        data class LoadingSuccess(val listType: MovieListType, val data: List<Movie>) : MovieEvent()

        sealed class Action {
            object Init : MovieEvent()
            data class LoadMore(val page: Int) : MovieEvent()
        }
    }

    override val stateGraph = stateGraph {
        state<MovieState.Empty> {
            on<MovieEvent.Action.Init> { transitionTo(MovieState.Loading(values().toList()), SideEffect.of { getData() }) }
        }
        state<MovieState.Loading> {
            on<MovieEvent.Action.LoadMore> { dontTransition() }
            on<MovieEvent.InitSuccess> { transitionTo(MovieState.DataLoaded(DashboardContent(it.data.nowPlaying, it.data.upcoming, it.data.popular))) }
            on<MovieEvent.InitFailure> { transitionTo(MovieState.Error) }
            on<MovieEvent.LoadingSuccess> { transitionTo(MovieState.DataLoaded(it.data)) }
            on<MovieEvent.LoadingFailure> { dontTransition() }
        }
        state<MovieState.DataLoaded> {
            on<MovieEvent.Action.LoadMore> { transitionTo(MovieState.Loading(listOf(it.listType)), SideEffect.of {
                when(it.listType) {
                    NOW_PLAYING -> getNowPlaying()
                    POPULAR -> getPopular()
                    UPCOMING -> getUpcoming()
                }
            }) }
        }
        state<MovieState.PageLoaded> {
            on<MovieEvent.Action.LoadMore> { transitionTo(MovieState.Loading(listOf(it.listType)), SideEffect.of {
                when(it.listType) {
                    NOW_PLAYING -> getNowPlaying()
                    POPULAR -> getPopular()
                    UPCOMING -> getUpcoming()
                }
            }) }
        }
        state<MovieState.Error> {}
    }

    private fun getData() = launch {
        Single.zip(
            movieDBRepository.getNowPlaying(),
            movieDBRepository.getUpcoming(),
            movieDBRepository.getPopular(),
            Function3 { nowPlaying: ListResponse<Movie>, upcoming: ListResponse<Movie>, popular: ListResponse<Movie> ->
                DashboardContent(nowPlaying, upcoming, popular)
            })
            .subscribe(
                { content ->
                    totalNowPlayingPage = content.nowPlaying.totalPages
                    totalUpcomingPage = content.upcoming.totalPages
                    totalPopularPage = content.popular.totalPages

                    isLoadingNowPlaying = false
                    isLoadingUpcoming = false
                    isLoadingPopular = false

                    currentNowPlayingPage++
                    currentUpcomingPage++
                    currentPopularPage++
                    invokeAction(MovieEvent.InitSuccess(content))
                },
                { invokeAction(MovieEvent.InitFailure) })
    }
}