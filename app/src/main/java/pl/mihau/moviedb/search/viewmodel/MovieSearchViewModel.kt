package pl.mihau.moviedb.search.viewmodel

import io.reactivex.rxkotlin.subscribeBy
import pl.mihau.moviedb.api.MovieDBRepository
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.util.state.Event
import pl.mihau.moviedb.util.state.SideEffect
import pl.mihau.moviedb.util.state.State
import pl.mihau.moviedb.util.state.StatefulViewModel

class MovieSearchViewModel(private val movieDBRepository: MovieDBRepository) : StatefulViewModel<MovieSearchViewModel.SearchState, MovieSearchViewModel.SearchEvent>(SearchState.Empty) {

    sealed class SearchState : State {
        object Empty : SearchState()
        object Loading : SearchState()
        data class DataLoaded(val keyword: String, val totalPages: Int, val page: Int, val data: List<Movie>) : SearchState()
        data class Error(val throwable: Throwable) : SearchState()
    }

    sealed class SearchEvent : Event {
        data class LoadingQueryFailure(val throwable: Throwable) : SearchEvent()
        data class LoadingQuerySuccess(val keyword: String, val totalPages: Int, val page: Int, val data: List<Movie>) : SearchEvent()

        sealed class Action {
            data class Query(val query: String) : SearchEvent()
            object LoadNewPage : SearchEvent()
            object Clear : SearchEvent()
        }
    }

    override val stateGraph = stateGraph {
        globalEvents {
            on<SearchEvent.Action.Clear> { transitionTo(SearchState.Empty, SideEffect.of { clearDisposables() }) }
        }

        state<SearchState.Empty> {
            on<SearchEvent.Action.Query> { transitionTo(SearchState.Loading, SideEffect.of { search(it.query) }) }
            on<SearchEvent.Action.LoadNewPage> { transitionTo(SearchState.Loading) }
        }
        state<SearchState.Loading> {
            on<SearchEvent.Action.Query> { dontTransition(SideEffect.of {
                clearDisposables()
                search(it.query)
            })}
            on<SearchEvent.Action.LoadNewPage> { dontTransition() }
            on<SearchEvent.LoadingQuerySuccess> { transitionTo(SearchState.DataLoaded(it.keyword, it.totalPages, it.page, it.data)) }
            on<SearchEvent.LoadingQueryFailure> { transitionTo(SearchState.Error(it.throwable)) }
        }
        state<SearchState.DataLoaded> {
            on<SearchEvent.Action.Query> { transitionTo(SearchState.Loading, SideEffect.of {
                clearDisposables()
                search(it.query)
            })}
            on<SearchEvent.Action.LoadNewPage> { transitionTo(SearchState.Loading, SideEffect.of { search(keyword, page + 1) }) }
        }
        state<SearchState.Error> {
            on<SearchEvent.Action.Query> { transitionTo(SearchState.Loading, SideEffect.of { search(it.query) }) }
        }
    }

    private fun search(query: String, currentPage: Int = 1) {
        if (currentPage == 1) {
            invokeAction(SearchEvent.Action.Clear)
            invokeAction(SearchEvent.Action.LoadNewPage)
        }

        launch {
            movieDBRepository.searchMovie(query, currentPage)
                .subscribeBy(
                    onSuccess = { invokeAction(when {
                        it.results.isEmpty() -> SearchEvent.Action.Clear
                        else -> SearchEvent.LoadingQuerySuccess(
                            keyword = query,
                            totalPages = it.totalPages,
                            page = it.page,
                            data = it.results
                        )})
                    },
                    onError = { invokeAction(SearchEvent.LoadingQueryFailure(it)) }
                )
        }
    }
}