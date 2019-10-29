package pl.mihau.moviedb.util.extension

import pl.mihau.moviedb.util.state.StateMachine

inline fun <STATE : Any, EVENT : Any> StateMachine.Transition<STATE, EVENT>.requireValidTransition(): StateMachine.Transition.Valid<STATE, EVENT>? {
    return this as? StateMachine.Transition.Valid
}
