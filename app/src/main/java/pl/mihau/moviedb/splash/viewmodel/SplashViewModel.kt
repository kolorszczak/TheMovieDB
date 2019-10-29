package pl.mihau.moviedb.splash.viewmodel

import kotlinx.android.parcel.Parcelize
import pl.mihau.moviedb.util.extension.requireValidTransition
import pl.mihau.moviedb.util.state.Event
import pl.mihau.moviedb.util.state.SingleLiveEvent
import pl.mihau.moviedb.util.state.State
import pl.mihau.moviedb.util.state.StatefulViewModel

class SplashViewModel : StatefulViewModel<SplashViewModel.SplashState, SplashViewModel.SplashEvent>
    (initialState = SplashState.ShowLogo) {

    val currentState = SingleLiveEvent<SplashState>()

    sealed class SplashState : State {
        @Parcelize object ShowLogo : SplashState()
        @Parcelize object Dashboard : SplashState()
        @Parcelize object Error : SplashState()
    }

    sealed class SplashEvent : Event {
        object PermissionGranted : SplashEvent()
        data class PermissionNotGranted(val shouldShowRequest: Boolean) : SplashEvent()
    }

    override val stateGraph = stateGraph {
        state<SplashState.ShowLogo> {
            on<SplashEvent.PermissionGranted> { transitionTo(SplashState.Dashboard) }
            on<SplashEvent.PermissionNotGranted> { transitionTo(SplashState.Error) }
        }
        state<SplashState.Dashboard> {}
        state<SplashState.Error> {}

        onTransition { transition ->
            transition.requireValidTransition()?.let { currentState.postValue(it.toState) }
        }
    }
}