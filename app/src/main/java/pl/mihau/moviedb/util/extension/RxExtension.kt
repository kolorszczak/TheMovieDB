package pl.mihau.moviedb.util.extension

import io.reactivex.Single
import pl.mihau.moviedb.util.provider.SchedulerProvider

fun <T> Single<T>.schedule(schedulerProvider: SchedulerProvider): Single<T> = this.subscribeOn(schedulerProvider.io())
    .observeOn(schedulerProvider.ui())