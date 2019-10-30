package pl.mihau.moviedb.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.android.parcel.Parcelize
import pl.mihau.moviedb.api.MovieDBRepository
import pl.mihau.moviedb.common.Strings.empty
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.util.state.Event
import pl.mihau.moviedb.util.state.SideEffect
import pl.mihau.moviedb.util.state.State
import pl.mihau.moviedb.util.state.StatefulViewModel
import timber.log.Timber


class MovieSearchViewModel(private val movieDBRepository: MovieDBRepository) : StatefulViewModel<MovieSearchViewModel.SearchState, MovieSearchViewModel.SearchEvent>(SearchState.Empty) {

    var keyword: String = empty

    var currentPage = 1
    var totalPages = 0
    var isLoading = false

    sealed class SearchState : State {
        @Parcelize object Empty : SearchState()
        @Parcelize object Loading : SearchState()
        @Parcelize data class DataLoaded(val data: List<Movie>) : SearchState()
        @Parcelize object Error : SearchState()
    }

    sealed class SearchEvent : Event {
        object LoadingQueryFailure : SearchEvent()
        data class LoadingQuerySuccess(val data: List<Movie>) : SearchEvent()

        sealed class Action {
            data class Query(val query: String) : SearchEvent()
            object LoadNewPage : SearchEvent()
            object Clear : SearchEvent()
        }
    }

    override val stateGraph = stateGraph {
        globalEvents {
            on<SearchEvent.Action.Clear> { transitionTo(SearchState.Empty, SideEffect.of { clear() }) }
        }

        state<SearchState.Empty> {
            on<SearchEvent.Action.Query> { transitionTo(SearchState.Loading, SideEffect.of { search(it.query) }) }
            on<SearchEvent.Action.LoadNewPage> { transitionTo(SearchState.Loading, SideEffect.of { Timber.d("TOOO") }) }
        }
        state<SearchState.Loading> {
            on<SearchEvent.Action.Query> { dontTransition(SideEffect.of {
                clear()
                search(it.query)
            })}
            on<SearchEvent.Action.LoadNewPage> { dontTransition() }
            on<SearchEvent.LoadingQuerySuccess> { transitionTo(SearchState.DataLoaded(it.data)) }
            on<SearchEvent.LoadingQueryFailure> { transitionTo(SearchState.Error) }
        }
        state<SearchState.DataLoaded> {
            on<SearchEvent.Action.Query> { transitionTo(SearchState.Loading, SideEffect.of {
                clear()
                search(it.query)
            })}
            on<SearchEvent.Action.LoadNewPage> { transitionTo(SearchState.Loading, SideEffect.of { search(keyword) }) }
        }
        state<SearchState.Error> {
            on<SearchEvent.Action.Query> { transitionTo(SearchState.Loading, SideEffect.of { search(it.query) }) }
        }
    }

    private fun clear() {
        isLoading = false
        keyword = empty
        currentPage = 1
        totalPages = 0
        clearDisposables()
    }

    private fun search(query: String) {
        if (!isLoading || !(query == keyword && currentPage == 1)) {

            if (currentPage == 1) {
                invokeAction(SearchEvent.Action.Clear)
            }

            isLoading = true
            keyword = query

            if (currentPage == 1) {
                invokeAction(SearchEvent.Action.LoadNewPage)
            }

            launch {
                movieDBRepository.searchMovie(query, currentPage)
                    .subscribe({ result ->
                        totalPages = result.totalPages
                        isLoading = false
                        currentPage++
                        invokeAction(
                            if (result.results.isEmpty()) SearchEvent.Action.Clear
                            else SearchEvent.LoadingQuerySuccess(result.results)
                        )
                    }, {
                        isLoading = false
                        invokeAction(SearchEvent.LoadingQueryFailure)
                    })
            }
        }
    }
}