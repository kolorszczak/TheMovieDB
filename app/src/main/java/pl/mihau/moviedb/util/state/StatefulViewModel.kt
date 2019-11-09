package pl.mihau.moviedb.util.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import pl.mihau.moviedb.util.extension.filter
import pl.mihau.moviedb.util.viewmodel.BaseViewModel
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.collections.getOrElse
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.reflect.KClass

abstract class StatefulViewModel<STATE : State, EVENT : Event>(
    private val initialState: STATE,
    private val handle: SavedStateHandle? = null
) : BaseViewModel() {

    companion object {
        private const val STATE_KEY = "STATE_KEY"
    }

    private lateinit var stateMachine: StateMachine<STATE, EVENT>

    private val _state = MutableLiveData<STATE>()
    val state: LiveData<STATE> = _state

    abstract val stateGraph: StateMachine.Graph<STATE, EVENT>

    init {
        val state = handle?.get<STATE>(STATE_KEY) ?: initialState
        _state.postValue(state)
    }

    fun stateGraph(
        graphDefinition: StateMachine.GraphBuilder<STATE, EVENT>.() -> Unit
    ): StateMachine.Graph<STATE, EVENT> {
        val graph =
            StateMachine.GraphBuilder<STATE, EVENT>(null)
                .apply {
                    initialState(handle?.get<STATE>(STATE_KEY) ?: initialState)
                    graphDefinition()
                }
                .build()
        stateMachine = StateMachine.create(graph) {
            onValidTransition { transition ->
                handle?.set(STATE_KEY, transition.toState)
                if (transition.toState != _state.value) {
                    _state.postValue(transition.toState)
                }
            }
        }
        return graph
    }

    fun invokeAction(action: EVENT) {
        if (::stateMachine.isInitialized) {
            stateMachine.transition(action)
        } else {
            error("You have to provide state machine graph")
        }
    }

    inline fun <reified STATE1 : STATE, VALUE> LiveData<STATE>.bindState(
        noinline transformer: (state: STATE1?) -> VALUE?
    ): LiveData<VALUE> {
        return Transformations.map<STATE, VALUE>(this) {
            transformer(if (value is STATE1) value as STATE1 else null)
        }
    }
    fun <S : Any, VALUE> LiveData<S>.bind(block: Binder<S, VALUE>.() -> Binder.Builder<S, VALUE>) =
        Binder<S, VALUE>(this).let(block).build()

    class Binder<K : Any, VALUE>(private val liveData: LiveData<K>) {

        val transformers: MutableMap<KClass<*>, Function1<*, VALUE>> = mutableMapOf()

        inline fun <reified S1 : K> instance(noinline transformer: Function1<S1, VALUE>) {
            if (transformers[S1::class] != null) throw RuntimeException("Should not override already present key")
            transformers[S1::class] = transformer
        }

        fun default(transformer: Function0<VALUE?>): Builder<K, VALUE> =
            Builder(liveData, transformers, transformer)

        fun ignoreUndefined() = Builder(liveData, transformers)

        class Builder<S : Any, VALUE>(
            private val liveData: LiveData<S>,
            private val transformers: Map<KClass<*>, Function1<*, VALUE>>,
            private val defaultTransformer: Function0<VALUE?>? = null
        ) {

            @Suppress("UNCHECKED_CAST")
            fun build(): LiveData<VALUE> = Transformations
                .map<S, VALUE>(liveData) { s ->
                    val transformer = transformers.getOrElse(s::class) { null } as? Function1<S, VALUE>
                    transformer
                        ?.invoke(s)
                        ?: defaultTransformer?.invoke()
                }
                .filter { (defaultTransformer == null && it != null) || defaultTransformer != null }
        }
    }
}