package pl.mihau.moviedb.util.rx

import io.reactivex.CompletableTransformer

interface TransformersCompletable {

    fun asyncSchedulers(): CompletableTransformer
}
