package pl.mihau.moviedb.list.viewmodel

import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.parcel.Parcelize
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
        @Parcelize object Empty : MovieState()
        @Parcelize object Loading : MovieState()
        @Parcelize data class DataLoaded(val data: ListResponse<Movie>) : MovieState()
        @Parcelize object Error : MovieState()
    }

    sealed class MovieEvent : Event {
        object InitFailure : MovieEvent()
        object LoadingFailure : MovieEvent()
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
            on<MovieEvent.InitFailure> { transitionTo(MovieState.Error) }
            on<MovieEvent.LoadingSuccess> { transitionTo(MovieState.DataLoaded(it.data)) }
            on<MovieEvent.LoadingFailure> { dontTransition() }
        }
        state<MovieState.DataLoaded> {
            on<MovieEvent.Action.LoadMore> { transitionTo(MovieState.Loading, SideEffect.of { getData(data.page + 1) }) }   }
        state<MovieState.Error> {}
    }

    private fun getData(page: Int = 1) = launch {
        when (listType) {
            NOW_PLAYING -> movieDBRepository.getNowPlaying(page)
            POPULAR -> movieDBRepository.getPopular(page)
            UPCOMING -> movieDBRepository.getUpcoming(page)
        }.subscribeBy(
            onError = { invokeAction(MovieEvent.LoadingFailure) },
            onSuccess = { invokeAction(MovieEvent.LoadingSuccess(it)) }
        )
    }
}