package pl.mihau.moviedb.util.extension

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import pl.mihau.moviedb.util.state.StatefulViewModel

inline fun <reified T : StatefulViewModel<*, *>> Module.savedStateViewModel(noinline definition: Definition<T>): BeanDefinition<T> {
    return viewModel(useState = true, definition = definition)
}