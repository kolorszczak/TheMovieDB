package pl.mihau.moviedb.util.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel(), DisposablesOwner {

    override val disposables = CompositeDisposable()

    override fun onCleared() {
        clearDisposables()
        super.onCleared()
    }
}