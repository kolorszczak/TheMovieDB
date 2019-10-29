package pl.mihau.moviedb.util.rx

import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TransformersSingleImpl : TransformersSingle {

    override fun <T> asyncSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
