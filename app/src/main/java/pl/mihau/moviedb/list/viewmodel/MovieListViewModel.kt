package pl.mihau.moviedb.list.viewmodel

import io.reactivex.rxkotlin.subscribeBy
import pl.mihau.moviedb.api.ListResponse
import pl.mihau.moviedb.api.MovieDBRepository
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.list.model.MovieListType
import pl.mihau.moviedb.list.model.MovieListType.*
import pl.mihau.moviedb.util.state.Event
import pl.mihau.moviedb.util.state.SideEffect
import pl.mihau.moviedb.util.state.State
import pl.mihau.moviedb.util.state.StatefulViewModel

class MovieListViewModel(private val movieDBRepository: MovieDBRepository) : StatefulViewModel<MovieListViewModel.MovieState, MovieListViewModel.MovieEvent>(MovieState.Empty) {

    lateinit var listType: MovieListType

    sealed class MovieState : State {
        object Empty : MovieState()
        object Loading : MovieState()
        data class DataLoaded(val data: ListResponse<Movie>) : MovieState()
        data class Error(val throwable: Throwable) : MovieState()
    }

    sealed class MovieEvent : Event {
        data class LoadingFailure(val throwable: Throwable) : MovieEvent()
        data class LoadingSuccess(val data: ListResponse<Movie>) : MovieEvent()

        sealed class Action {
            object Init : MovieEvent()
            data class LoadMore(val page: Int) : MovieEvent()
        }
    }

    override val stateGraph = stateGraph {
        state<MovieState.Empty> {
            on<MovieEvent.Action.Init> { transitionTo(MovieState.Loading, SideEffect.of { getData() }) }
        }
        state<MovieState.Loading> {
            on<MovieEvent.Action.LoadMore> { dontTransition() }
            on<MovieEvent.LoadingSuccess> { transitionTo(MovieState.DataLoaded(it.data)) }
            on<MovieEvent.LoadingFailure> { transitionTo(MovieState.Error(it.throwable)) }
        }
        state<MovieState.DataLoaded> {
            on<MovieEvent.Action.LoadMore> { transitionTo(MovieState.Loading, SideEffect.of { getData(data.page + 1) }) }   }
        state<MovieState.Error> {
            on<MovieEvent.Action.Init> { transitionTo(MovieState.Loading, SideEffect.of { getData() }) }
        }
    }

    private fun getData(page: Int = 1) = launch {
        when (listType) {
            NOW_PLAYING -> movieDBRepository.getNowPlaying(page)
            POPULAR -> movieDBRepository.getPopular(page)
            UPCOMING -> movieDBRepository.getUpcoming(page)
        }.subscribeBy(
            onError = { invokeAction(MovieEvent.LoadingFailure(it)) },
            onSuccess = { invokeAction(MovieEvent.LoadingSuccess(it)) }
        )
    }
}