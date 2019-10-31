package pl.mihau.moviedb.util.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.mihau.moviedb.details.viewmodel.MovieDetailsViewModel
import pl.mihau.moviedb.list.viewmodel.MovieListViewModel
import pl.mihau.moviedb.search.viewmodel.MovieSearchViewModel
import pl.mihau.moviedb.splash.viewmodel.SplashViewModel

val viewModelModule = module {
    viewModel { SplashViewModel() }
    viewModel { MovieListViewModel(get()) }
    viewModel { MovieSearchViewModel(get()) }
    viewModel { MovieDetailsViewModel(get()) }
}