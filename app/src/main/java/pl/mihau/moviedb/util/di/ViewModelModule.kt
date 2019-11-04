package pl.mihau.moviedb.util.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.mihau.moviedb.details.viewmodel.MovieDetailsViewModel
import pl.mihau.moviedb.list.viewmodel.MovieListViewModel
import pl.mihau.moviedb.search.viewmodel.MovieSearchViewModel

val viewModelModule = module {
    viewModel { MovieListViewModel(movieDBRepository = get()) }
    viewModel { MovieSearchViewModel(movieDBRepository = get()) }
    viewModel { MovieDetailsViewModel(movieDBRepository = get()) }
}