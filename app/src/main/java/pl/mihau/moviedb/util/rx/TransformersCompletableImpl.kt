package pl.mihau.moviedb.util.rx

import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TransformersCompletableImpl : TransformersCompletable {

    override fun asyncSchedulers(): CompletableTransformer {
        return CompletableTransformer { upstream ->
            upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
