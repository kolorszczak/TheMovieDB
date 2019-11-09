package pl.mihau.moviedb.util.di

import org.koin.dsl.module
import pl.mihau.moviedb.util.provider.AppSchedulerProvider
import pl.mihau.moviedb.util.provider.SchedulerProvider

val rxModule = module {
    single<SchedulerProvider> { AppSchedulerProvider() }
}
