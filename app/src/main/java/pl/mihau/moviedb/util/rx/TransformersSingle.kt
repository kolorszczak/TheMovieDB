package pl.mihau.moviedb.util.rx

import io.reactivex.SingleTransformer

interface TransformersSingle {

    fun <T> asyncSchedulers(): SingleTransformer<T, T>
}
