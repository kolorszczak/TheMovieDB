package pl.mihau.moviedb.details.viewmodel

import kotlinx.android.parcel.Parcelize
import pl.mihau.moviedb.api.MovieDBRepository
import pl.mihau.moviedb.details.model.MovieDetails
import pl.mihau.moviedb.util.state.Event
import pl.mihau.moviedb.util.state.SideEffect
import pl.mihau.moviedb.util.state.State
import pl.mihau.moviedb.util.state.StatefulViewModel

class MovieDetailsViewModel(private val movieDBRepository: MovieDBRepository) :
    StatefulViewModel<MovieDetailsViewModel.DetailsState, MovieDetailsViewModel.DetailsEvent>(
        DetailsState.Empty
    ) {

    sealed class DetailsState : State {
        @Parcelize
        object Empty : DetailsState()

        @Parcelize
        object Loading : DetailsState()

        @Parcelize
        data class DataLoaded(val data: MovieDetails) : DetailsState()

        @Parcelize
        object Error : DetailsState()
    }

    sealed class DetailsEvent : Event {
        object LoadingFailure : DetailsEvent()
        data class LoadingSuccess(val data: MovieDetails) : DetailsEvent()

        sealed class Action {
            data class Load(val id: Int) : DetailsEvent()
        }
    }

    override val stateGraph = stateGraph {
        state<DetailsState.Empty> {
            on<DetailsEvent.Action.Load> {
                transitionTo(
                    DetailsState.Loading,
                    SideEffect.of { getMovieDetails(it.id) })
            }
        }
        state<DetailsState.Loading> {
            on<DetailsEvent.LoadingSuccess> { transitionTo(DetailsState.DataLoaded(it.data)) }
            on<DetailsEvent.LoadingFailure> { transitionTo(DetailsState.Error) }
        }
        state<DetailsState.DataLoaded> {}
        state<DetailsState.Error> {}
    }

    private fun getMovieDetails(id: Int) {
        launch {
            movieDBRepository.getMovieDetails(id)
                .subscribe(
                    { response -> invokeAction(DetailsEvent.LoadingSuccess(response)) },
                    { invokeAction(DetailsEvent.LoadingFailure) })
        }
    }
}