package pl.mihau.moviedb.util.di

import org.koin.dsl.module
import pl.mihau.moviedb.api.MovieDBRepository

val repositoryModule = module {
    single { MovieDBRepository(get(), get()) }
}