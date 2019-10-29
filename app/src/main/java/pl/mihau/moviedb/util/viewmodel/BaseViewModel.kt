package pl.mihau.moviedb.util.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.get
import pl.mihau.moviedb.util.rx.TransformersCompletable
import pl.mihau.moviedb.util.rx.TransformersObservable
import pl.mihau.moviedb.util.rx.TransformersSingle

abstract class BaseViewModel : ViewModel(), KoinComponent {

    protected val transformersSingle by lazy { get<TransformersSingle>() }

    protected val transformersCompletable by lazy { get<TransformersCompletable>() }

    protected val transformersObservable by lazy { get<TransformersObservable>() }

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}