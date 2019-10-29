package pl.mihau.moviedb.util.di

import org.koin.dsl.module
import pl.mihau.moviedb.util.rx.*

val rxModule = module {
    single<TransformersCompletable> { TransformersCompletableImpl() }
    single<TransformersObservable> { TransformersObservableImpl() }
    single<TransformersSingle> { TransformersSingleImpl() }
}
