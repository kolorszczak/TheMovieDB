package pl.mihau.moviedb.util.di

import org.koin.dsl.module
import pl.mihau.moviedb.util.storage.StorageManager

val storageModule = module {
    single { StorageManager() }
}
