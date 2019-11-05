package pl.mihau.moviedb.search.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import pl.mihau.moviedb.api.ListResponse
import pl.mihau.moviedb.api.MovieDBRepository
import pl.mihau.moviedb.list.model.Movie
import pl.mihau.moviedb.util.di.repositoryModule


class MovieSearchViewModelTest : KoinTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        startKoin { modules(repositoryModule) }
    }

    @After
    fun clear() {
        stopKoin()
    }

    @Test
    fun `test clear query`() {
        // given
        val viewModel = MovieSearchViewModel(movieDBRepository = declareMock())

        //when
        viewModel.invokeAction(MovieSearchViewModel.SearchEvent.Action.Clear)

        //then
        val expected = MovieSearchViewModel.SearchState.Empty
        assertEquals(viewModel.state.value, expected)
    }

    @Test
    fun `test query empty result`() {
        // given
        val result: Single<ListResponse<Movie>> = Single.just(ListResponse(
            page = 1,
            totalPages = 1,
            totalResults = 0,
            results = emptyList()
        ))
        val movieDBRepository = declareMock<MovieDBRepository> {
            given(this.searchMovie("test", 1)).willReturn(result)
        }
        val viewModel = MovieSearchViewModel(movieDBRepository = movieDBRepository)

        //when
        viewModel.invokeAction(MovieSearchViewModel.SearchEvent.Action.Query("test"))

        //then
        val expected = MovieSearchViewModel.SearchState.Empty
        assertEquals(viewModel.state.value, expected)
    }

    @Test
    fun `test query failure`() {
        // given
        val result: Single<ListResponse<Movie>> = Single.error(Error("error"))
        val movieDBRepository = declareMock<MovieDBRepository> {
            given(this.searchMovie("test", 1)).willReturn(result)
        }

        val viewModel = MovieSearchViewModel(movieDBRepository = movieDBRepository)

        //when
        viewModel.invokeAction(MovieSearchViewModel.SearchEvent.Action.Query("test"))

        //then
        val expected = MovieSearchViewModel.SearchState.Error
        assertEquals(viewModel.state.value, expected)
    }

    @Test
    fun `test query success`() {
        // given
        val emptyMovie = mock(Movie::class.java)
        val result: Single<ListResponse<Movie>> = Single.just(ListResponse(
            page = 1,
            totalPages = 1,
            totalResults = 1,
            results = listOf(emptyMovie)
        ))
        val movieDBRepository = declareMock<MovieDBRepository> {
            given(this.searchMovie("test", 1)).willReturn(result)
        }

        val viewModel = MovieSearchViewModel(movieDBRepository = movieDBRepository)

        //when
        viewModel.invokeAction(MovieSearchViewModel.SearchEvent.Action.Query("test"))

        //then
        val expected = MovieSearchViewModel.SearchState.DataLoaded(
            keyword = "test",
            page = 1,
            totalPages = 1,
            data = listOf(emptyMovie)
        )
        assertEquals(viewModel.state.value, expected)
    }

    @Test
    fun `test loading new page`() {
        // given
        val emptyMovie = mock(Movie::class.java)
        val firstPage: Single<ListResponse<Movie>> = Single.just(ListResponse(
            page = 1,
            totalPages = 2,
            totalResults = 2,
            results = listOf(emptyMovie)
        ))
        val secondPage: Single<ListResponse<Movie>> = Single.just(ListResponse(
            page = 2,
            totalPages = 2,
            totalResults = 2,
            results = listOf(emptyMovie)
        ))
        val movieDBRepository = declareMock<MovieDBRepository> {
            given(this.searchMovie("test", 1)).willReturn(firstPage)
            given(this.searchMovie("test", 2)).willReturn(secondPage)
        }
        val viewModel = MovieSearchViewModel(movieDBRepository = movieDBRepository)

        //when
        viewModel.invokeAction(MovieSearchViewModel.SearchEvent.Action.Query("test"))
        viewModel.invokeAction(MovieSearchViewModel.SearchEvent.Action.LoadNewPage)

        //then
        val expected = MovieSearchViewModel.SearchState.DataLoaded(
            keyword = "test",
            page = 2,
            totalPages = 2,
            data = listOf(emptyMovie)
        )
        assertEquals(viewModel.state.value, expected)
    }

    @Test
    fun `test loading new page failure`() {
        // given
        val emptyMovie = mock(Movie::class.java)
        val firstPage: Single<ListResponse<Movie>> = Single.just(ListResponse(
            page = 1,
            totalPages = 2,
            totalResults = 2,
            results = listOf(emptyMovie)
        ))
        val secondPage: Single<ListResponse<Movie>> = Single.error(Error("error"))
        val movieDBRepository = declareMock<MovieDBRepository> {
            given(this.searchMovie("test", 1)).willReturn(firstPage)
            given(this.searchMovie("test", 2)).willReturn(secondPage)
        }
        val viewModel = MovieSearchViewModel(movieDBRepository = movieDBRepository)

        //when
        viewModel.invokeAction(MovieSearchViewModel.SearchEvent.Action.Query("test"))
        viewModel.invokeAction(MovieSearchViewModel.SearchEvent.Action.LoadNewPage)

        //then
        val expected = MovieSearchViewModel.SearchState.Error
        assertEquals(viewModel.state.value, expected)
    }
}