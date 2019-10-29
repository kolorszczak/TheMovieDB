package pl.mihau.moviedb.util.di

import org.koin.dsl.bind
import org.koin.dsl.module
import pl.mihau.moviedb.util.lifecycle.ActivityLifecycleManager
import pl.mihau.moviedb.util.lifecycle.ActivityLifecycleObservable
import pl.mihau.moviedb.util.lifecycle.AppVisibilityResolver

val applicationUtilsModule = module {
    single { ActivityLifecycleManager(get()) }
    single { AppVisibilityResolver() } bind ActivityLifecycleObservable::class
}
