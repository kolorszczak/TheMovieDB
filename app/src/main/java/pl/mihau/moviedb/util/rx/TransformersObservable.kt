package pl.mihau.moviedb.util.rx

import io.reactivex.ObservableTransformer

interface TransformersObservable {

    fun <T> asyncSchedulers(): ObservableTransformer<T, T>
}
