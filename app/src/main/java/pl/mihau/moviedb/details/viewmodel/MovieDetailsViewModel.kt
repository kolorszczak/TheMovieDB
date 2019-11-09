package pl.mihau.moviedb.details.viewmodel

import io.reactivex.rxkotlin.subscribeBy
import pl.mihau.moviedb.api.MovieDBRepository
import pl.mihau.moviedb.details.model.MovieDetails
import pl.mihau.moviedb.util.state.Event
import pl.mihau.moviedb.util.state.SideEffect
import pl.mihau.moviedb.util.state.State
import pl.mihau.moviedb.util.state.StatefulViewModel

class MovieDetailsViewModel(private val movieDBRepository: MovieDBRepository) : StatefulViewModel<MovieDetailsViewModel.DetailsState, MovieDetailsViewModel.DetailsEvent>(DetailsState.Empty) {

    sealed class DetailsState : State {
        object Empty : DetailsState()
        object Loading : DetailsState()
        data class DataLoaded(val data: MovieDetails) : DetailsState()
        data class Error(val throwable: Throwable) : DetailsState()
    }

    sealed class DetailsEvent : Event {
        data class LoadingFailure(val throwable: Throwable) : DetailsEvent()
        data class LoadingSuccess(val data: MovieDetails) : DetailsEvent()

        sealed class Action {
            data class Load(val id: Int) : DetailsEvent()
        }
    }

    override val stateGraph = stateGraph {
        state<DetailsState.Empty> {
            on<DetailsEvent.Action.Load> { transitionTo(DetailsState.Loading, SideEffect.of { getMovieDetails(it.id) })}
        }
        state<DetailsState.Loading> {
            on<DetailsEvent.LoadingSuccess> { transitionTo(DetailsState.DataLoaded(it.data)) }
            on<DetailsEvent.LoadingFailure> { transitionTo(DetailsState.Error(it.throwable)) }
        }
        state<DetailsState.DataLoaded> {}
        state<DetailsState.Error> {}
    }

    private fun getMovieDetails(id: Int) {
        launch {
            movieDBRepository.getMovieDetails(id)
                .subscribeBy(
                    onSuccess = { response -> invokeAction(DetailsEvent.LoadingSuccess(response)) },
                    onError = { invokeAction(DetailsEvent.LoadingFailure(it)) })
        }
    }
}