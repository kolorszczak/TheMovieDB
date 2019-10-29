package pl.mihau.moviedb.util.state

data class StateChanger<S>(
    val state: S,
    val sideEffect: SideEffect = SideEffect.empty
)

infix fun <S> S.with(sideEffect: SideEffect): StateChanger<S> = StateChanger(this, sideEffect)