package pl.mihau.moviedb.util.state

data class StateChanger<S>(
    val state: S,
    val sideEffect: SideEffect = SideEffect.empty
)